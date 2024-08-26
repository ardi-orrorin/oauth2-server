package com.ardi.oauth2.config

import com.ardi.oauth2.dto.UserDetailsDto
import com.ardi.oauth2.service.OidcUserService
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*


@Configuration
@EnableWebSecurity
class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun securityFilterChain(
        http: HttpSecurity,
    ): SecurityFilterChain? {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)

        val userInfoMapper: (OidcUserInfoAuthenticationContext) -> OidcUserInfo = { context ->
            val authentication = context.getAuthentication() as Authentication
            val principal =  authentication.principal as JwtAuthenticationToken
            OidcUserInfo(principal.token.claims)
        }

        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc {
                it.userInfoEndpoint {
                    it.userInfoMapper(userInfoMapper)
                }
            }
            .authorizationEndpoint {
                it.consentPage("/oauth2/consent")
            }

        http.exceptionHandling { exceptions ->
            exceptions.defaultAuthenticationEntryPointFor( // 인가 실패에 대한 처리를 정의
                LoginUrlAuthenticationEntryPoint("/login"),
                MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            )
        }

        http.oauth2ResourceServer { it.jwt(withDefaults()) }

        return http.formLogin(withDefaults()).build();
    }

    @Bean
    @Order(2)
    fun springFilterChain(
        http: HttpSecurity,
    ): SecurityFilterChain? {

        http.authorizeHttpRequests {
            it.requestMatchers("/login**", "/signup**", "/error", "/logout", "/css/**", "/consent", "/oauth2/**").permitAll()
            it.requestMatchers("/" ).authenticated()
            it.anyRequest().authenticated()

        }
        .formLogin {
            it.loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll()
        }

        http.csrf { it.disable() }
            .cors(withDefaults())

        return http.build()
    }


    @Bean
    fun oAuth2TokenCustomizer(userinfoService: OidcUserService): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context ->
            val authentication = context.getPrincipal() as Authentication
            val principal =  authentication.principal as UserDetailsDto
            val user = userinfoService.loadUserByUsername(principal.username, context.authorizedScopes)

            context.claims.claims {
                it.putAll(user.claims)
            }
        }
    }

     @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()

        config.allowedMethods = listOf(
            "GET",
            "POST",
        )
        config.allowedHeaders = listOf(
            "Authorization",
            "Content-Type",
        )
        config.allowCredentials = true
        config.maxAge = 3600L
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder().build()
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey = generateRsa()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector, _ -> jwkSelector.select(jwkSet) }
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext>): JwtEncoder =
        NimbusJwtEncoder(jwkSource)

    private fun generateRsa(): RSAKey {
        val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }


}