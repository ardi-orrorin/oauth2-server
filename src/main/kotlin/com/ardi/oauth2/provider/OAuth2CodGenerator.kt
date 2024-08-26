package com.ardi.oauth2.provider

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator
import org.springframework.security.crypto.keygen.StringKeyGenerator
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import java.time.Instant
import java.util.*


class OAuth2CodGenerator: OAuth2TokenGenerator<OAuth2AuthorizationCode> {

    private val authorizationCodeGenerator: StringKeyGenerator = Base64StringKeyGenerator(
        Base64.getUrlEncoder().withoutPadding(), 96
    )

    override fun generate(context: OAuth2TokenContext?): OAuth2AuthorizationCode? {
//        if (context!!.tokenType == null || OAuth2ParameterNames.CODE != context!!.tokenType.value) {
//            return null
//        }
        val issuedAt = Instant.now()
        val expiresAt = issuedAt
            .plus(context!!.registeredClient.tokenSettings.authorizationCodeTimeToLive)
        return OAuth2AuthorizationCode(authorizationCodeGenerator.generateKey(), issuedAt, expiresAt)
    }
}