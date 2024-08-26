package com.ardi.oauth2.provider

import com.ardi.oauth2.service.RegisteredClientService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationConsentAuthenticationToken
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext
import org.springframework.stereotype.Component


//@Component
class CustomAuthenticationProvider(
    private val clientService: RegisteredClientService,
    private val authorizationService: OAuth2AuthorizationService,
): AuthenticationProvider {

    private val tokenGenerator = OAuth2CodGenerator()
    override fun authenticate(authentication: Authentication?): OAuth2AuthorizationCodeRequestAuthenticationToken? {
        if (authentication !is OAuth2AuthorizationConsentAuthenticationToken) {
            return null
        }
        val customCodeGrantAuthentication = authentication

        val clientPrincipal = customCodeGrantAuthentication.principal as Authentication

        val registeredClient = this.clientService.findByClientId(customCodeGrantAuthentication.clientId)
            ?: throw OAuth2AuthenticationException(OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT))

        val tokenContext: OAuth2TokenContext = DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(clientPrincipal)
            .tokenType(OAuth2TokenType.ACCESS_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrant(customCodeGrantAuthentication)
            .build()

        val generatedAccessToken: OAuth2AuthorizationCode? = tokenGenerator.generate(tokenContext)
        if (generatedAccessToken == null) {
            val error = OAuth2Error(
                OAuth2ErrorCodes.SERVER_ERROR,
                "The token generator failed to generate the access token.", null
            )
            throw OAuth2AuthenticationException(error)
        }
        val accessToken = OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.tokenValue, generatedAccessToken.issuedAt,
            generatedAccessToken.expiresAt, null
        )

        val authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
            .principalName(clientPrincipal.name)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        if (generatedAccessToken is ClaimAccessor) {
            authorizationBuilder.token(
                accessToken
            ) { metadata: MutableMap<String?, Any?> ->
                metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME] =
                    (generatedAccessToken as ClaimAccessor).claims
            }
        } else {
            authorizationBuilder.accessToken(accessToken)
        }
        val authorization = authorizationBuilder.build()

        this.authorizationService.save(authorization)

        return OAuth2AuthorizationCodeRequestAuthenticationToken(
            registeredClient.redirectUris.first(),
            customCodeGrantAuthentication.clientId,
            clientPrincipal,
            generatedAccessToken,
            registeredClient.redirectUris.first(),
            authentication.state,
            authentication.scopes
        )


    }

    override fun supports(authentication: Class<*>?): Boolean {
        return OAuth2AuthorizationConsentAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}