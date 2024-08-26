package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.AuthorizationRepository
import com.ardi.oauth2.entity.Authorization
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
class CustomOAuth2AuthorizationService(
    private val authorizationRepository: AuthorizationRepository,
    private val clientRepository: RegisteredClientRepository,
    private val objectMapper: ObjectMapper,
): OAuth2AuthorizationService {

   override fun save(authorization: OAuth2Authorization?) {
        Assert.notNull(authorization, "authorization not null")

        authorizationRepository.save(toEntity(authorization!!))
    }

    override fun remove(authorization: OAuth2Authorization?) {
        Assert.notNull(authorization, "authorization not null")
        authorizationRepository.deleteById(authorization!!.id)
    }

    override fun findById(id: String?): OAuth2Authorization? {
        Assert.notNull(id, "id not null")
        return authorizationRepository.findById(id!!)
            .map { it.toDto() }
            .orElse(null)
    }

    override fun findByToken(token: String?, tokenType: OAuth2TokenType?): OAuth2Authorization? {
        Assert.notNull(token, "token not null")
        Assert.notNull(tokenType, "tokenType not null")

        val result = when(tokenType?.value) {
            OAuth2ParameterNames.STATE -> token?.let{ authorizationRepository.findByState(token) }
            OAuth2ParameterNames.CODE -> token?.let{ authorizationRepository.findByAuthorizationCodeValue(token) }
            OAuth2ParameterNames.ACCESS_TOKEN -> token?.let{ authorizationRepository.findByAccessTokenValue(token) }
            OAuth2ParameterNames.REFRESH_TOKEN -> token?.let{ authorizationRepository.findByRefreshTokenValue(token) }
            OAuth2ParameterNames.TOKEN -> token?.let{ authorizationRepository.findByOidcIdTokenValue(token) }
            OAuth2ParameterNames.USER_CODE -> token?.let{ authorizationRepository.findByUserCodeValue(token) }
            OAuth2ParameterNames.DEVICE_CODE -> token?.let{ authorizationRepository.findByDeviceCodeValue(token) }
            else -> authorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(token!!)
        }

        return result?.toDto()
    }

    private fun toEntity(authorization: OAuth2Authorization): Authorization {


        val authorizationCode: OAuth2Authorization.Token<OAuth2AuthorizationCode>? = authorization.getToken(OAuth2AuthorizationCode::class.java)
//            ?: throw IllegalArgumentException("Invalid authorization code")

        val accessToken: OAuth2Authorization.Token<OAuth2AccessToken>? = authorization.getToken(OAuth2AccessToken::class.java)
//            ?: throw IllegalArgumentException("Invalid access token")

        val refreshToken: OAuth2Authorization.Token<OAuth2RefreshToken>? = authorization.getToken(OAuth2RefreshToken::class.java)
//            ?: throw IllegalArgumentException("Invalid refresh token")

        val oidcIdToken: OAuth2Authorization.Token<OidcIdToken>? = authorization.getToken(OidcIdToken::class.java)
//            ?: throw IllegalArgumentException("Invalid OIDC ID token")

        val userCode: OAuth2Authorization.Token<OAuth2UserCode>? = authorization.getToken(OAuth2UserCode::class.java)
//            ?: throw IllegalArgumentException("Invalid user code")

        val deviceCode: OAuth2Authorization.Token<OAuth2DeviceCode>? = authorization.getToken(OAuth2DeviceCode::class.java)
//            ?: throw IllegalArgumentException("Invalid device code")


        return Authorization(
            id                         = authorization.id,
            registeredClientId         = authorization.registeredClientId,
            principalName              = authorization.principalName,
            authorizationGrantType     = authorization.authorizationGrantType.value,
            attributes                 = objectMapper.writeValueAsString(authorization.attributes),
            state                      = authorization.getAttribute(OAuth2ParameterNames.STATE),
            authorizationCodeValue     = authorizationCode?.token?.tokenValue,
            authorizationCodeIssuedAt  = authorizationCode?.token?.issuedAt,
            authorizationCodeExpiresAt = authorizationCode?.token?.expiresAt,
            authorizationCodeMetadata  = authorizationCode?.metadata?.let { objectMapper.writeValueAsString(it) },
            authorizedScopes           = authorization.authorizedScopes?.joinToString(","),
            accessTokenValue           = accessToken?.token?.tokenValue,
            accessTokenIssuedAt        = accessToken?.token?.issuedAt,
            accessTokenExpiresAt       = accessToken?.token?.expiresAt,
            accessTokenMetadata        = accessToken?.metadata?.let { objectMapper.writeValueAsString(it) },
            accessTokenScopes          = accessToken?.token?.scopes?.joinToString(","),
            refreshTokenValue          = refreshToken?.token?.tokenValue,
            refreshTokenIssuedAt       = refreshToken?.token?.issuedAt,
            refreshTokenExpiresAt      = refreshToken?.token?.expiresAt,
            refreshTokenMetadata       = refreshToken?.metadata?.let { objectMapper.writeValueAsString(it) },
            oidcIdTokenValue           = oidcIdToken?.token?.tokenValue,
            oidcIdTokenIssuedAt        = oidcIdToken?.token?.issuedAt,
            oidcIdTokenExpiresAt       = oidcIdToken?.token?.expiresAt,
            oidcIdTokenMetadata        = oidcIdToken?.metadata?.let { objectMapper.writeValueAsString(it) },
            oidcIdTokenClaims          = oidcIdToken?.token?.claims?.let { objectMapper.writeValueAsString(it) },
            userCodeValue              = userCode?.token?.tokenValue,
            userCodeIssuedAt           = userCode?.token?.issuedAt,
            userCodeExpiresAt          = userCode?.token?.expiresAt,
            userCodeMetadata           = userCode?.metadata?.let { objectMapper.writeValueAsString(it) },
            deviceCodeValue            = deviceCode?.token?.tokenValue,
            deviceCodeIssuedAt         = deviceCode?.token?.issuedAt,
            deviceCodeExpiresAt        = deviceCode?.token?.expiresAt,
            deviceCodeMetadata         = deviceCode?.metadata?.let { objectMapper.writeValueAsString(it) },
        )
    }


    fun Authorization.toDto(): OAuth2Authorization {
        val client = clientRepository.findById(registeredClientId)
            ?: throw IllegalArgumentException("Invalid registered client id: $registeredClientId")

        val builder = OAuth2Authorization.withRegisteredClient(client)
            .id(this.id)
            .principalName(this.principalName)
            .authorizationGrantType(resolveGrantType(this.authorizationGrantType))
            .authorizedScopes(this.authorizedScopes?.split(",")?.toSet())
            .attributes {
                val attributes: Map<String, Any> = objectMapper.readValue(this.attributes, object : TypeReference<Map<String, Any>>() {})
                it.putAll(attributes)
            }

        if (this.state != null) {
            builder.attribute(OAuth2ParameterNames.STATE, this.state)
        }

        if(this.authorizationCodeValue != null) {
            val authorizationCode = OAuth2AuthorizationCode(
                this.authorizationCodeValue,
                this.authorizationCodeIssuedAt,
                this.authorizationCodeExpiresAt
            )
            builder.token(authorizationCode) {
                it.putAll(objectMapper.readValue(this.authorizationCodeMetadata, object : TypeReference<Map<String, Any>>(){}))
            }
        }

        if(this.accessTokenValue != null) {
            val accessToken = OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                this.accessTokenValue,
                this.accessTokenIssuedAt,
                this.accessTokenExpiresAt,
                this.accessTokenScopes?.split(",")?.toSet()
            )
            builder.token(accessToken) {
                it.putAll(objectMapper.readValue(this.accessTokenMetadata, object : TypeReference<Map<String, Any>>(){}))
            }
        }

        if(this.refreshTokenValue != null) {
            val refreshToken = OAuth2RefreshToken(
                this.refreshTokenValue,
                this.refreshTokenIssuedAt,
                this.refreshTokenExpiresAt
            )
            builder.token(refreshToken) {
                it.putAll(objectMapper.readValue(this.refreshTokenMetadata, object : TypeReference<Map<String, Any>>(){}))
            }
        }

        if(this.oidcIdTokenValue != null) {
            val oidcIdToken = OidcIdToken(
                this.oidcIdTokenValue,
                this.oidcIdTokenIssuedAt,
                this.oidcIdTokenExpiresAt,
                objectMapper.readValue(this.oidcIdTokenClaims, object : TypeReference<Map<String, Any>>(){}),
            )
            builder.token(oidcIdToken) {
                it.putAll(objectMapper.readValue(this.oidcIdTokenMetadata, object : TypeReference<Map<String, Any>>(){}))
            }
        }

        if(this.userCodeValue != null) {
            val userCode = OAuth2UserCode(
                this.userCodeValue,
                this.userCodeIssuedAt,
                this.userCodeExpiresAt
            )
            builder.token(userCode) {
                it.putAll(objectMapper.readValue(this.userCodeMetadata, object : TypeReference<Map<String, Any>>(){}))
            }
        }

        if(this.deviceCodeValue != null) {
            val deviceCode = OAuth2DeviceCode(
                this.deviceCodeValue,
                this.deviceCodeIssuedAt,
                this.deviceCodeExpiresAt
            )
            builder.token(deviceCode) {
                it.putAll(objectMapper.readValue(this.deviceCodeMetadata, object : TypeReference<Map<String, Any>>(){}))
            }
        }

        return builder.build()
    }

    fun resolveGrantType(authorizationGrantType: String): AuthorizationGrantType {
        return when (authorizationGrantType) {
            "authorization_code" -> AuthorizationGrantType.AUTHORIZATION_CODE
            "client_credentials" -> AuthorizationGrantType.CLIENT_CREDENTIALS
            "password" -> AuthorizationGrantType.PASSWORD
            "refresh_token" -> AuthorizationGrantType.REFRESH_TOKEN
            "urn:ietf:params:oauth:grant-type:device_code" -> AuthorizationGrantType.DEVICE_CODE
            "urn:ietf:params:oauth:grant-type:jwt-bearer" -> AuthorizationGrantType.JWT_BEARER
            "urn:ietf:params:oauth:grant-type:token-exchange" -> AuthorizationGrantType.TOKEN_EXCHANGE
            else -> throw IllegalArgumentException("Invalid authorization grant type: $authorizationGrantType")
        }
    }
}
