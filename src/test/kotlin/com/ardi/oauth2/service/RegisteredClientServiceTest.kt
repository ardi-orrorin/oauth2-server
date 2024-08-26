package com.ardi.oauth2.service

import com.ardi.oauth2.dto.CustomScope
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.util.*

@SpringBootTest
@ActiveProfiles("dev")
//@Transactional
class RegisteredClientServiceTest {

    @Autowired
    lateinit var registeredClientService: RegisteredClientService

    @Test
    fun save() {

        // clientName, redirectUris, scopes
        val clientName = "anamensis"
        val clientId = BCryptPasswordEncoder().encode("anamensis" + UUID.randomUUID().toString())
        val clientSecret = BCryptPasswordEncoder().encode(clientId + UUID.randomUUID().toString())

        println("clientId: $clientId")
        println("clientSecret: $clientSecret")

        val client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientName(clientName)
            .clientId("anamensis-client")
            .clientSecret(BCryptPasswordEncoder().encode("anamensis-client-secret"))
            .clientIdIssuedAt(Instant.now())
            .clientSecretExpiresAt(Instant.now().plus(Duration.ofDays(30)))
            .clientAuthenticationMethods {
                it.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                it.add(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            }
            .authorizationGrantTypes {
                it.add(AuthorizationGrantType.AUTHORIZATION_CODE)
                it.add(AuthorizationGrantType.REFRESH_TOKEN)
                it.add(AuthorizationGrantType.JWT_BEARER)
            }
            .redirectUris {
                it.add("http://localhost:3000/api/auth/callback/ardi")
            }
            .scopes {
                it.add(OidcScopes.OPENID)
                it.add(CustomScope.NAME)
                it.add(CustomScope.BIRTHDAY)
                it.add(CustomScope.PHONE)
                it.add(CustomScope.ADDRESS)
            }
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(true)
                .build()
            )
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofDays(1))
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .build()
            )
            .build()

        registeredClientService.save(client)
    }
}