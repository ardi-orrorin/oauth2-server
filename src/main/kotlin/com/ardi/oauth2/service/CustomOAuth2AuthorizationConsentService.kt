package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.AuthorizationConsentRepository
import com.ardi.oauth2.Repository.ClientRepository
import com.ardi.oauth2.entity.AuthorizationConsent
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.Assert


@Component
class CustomOAuth2AuthorizationConsentService(
    private val authorizationConsentRepository: AuthorizationConsentRepository,
    private val clientRepository: ClientRepository,
): OAuth2AuthorizationConsentService {

    override fun save(authorizationConsent: OAuth2AuthorizationConsent?) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null")
        authorizationConsentRepository.save(authorizationConsent!!.toEntity())
    }

    override fun remove(authorizationConsent: OAuth2AuthorizationConsent?) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null")
        authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(authorizationConsent!!.registeredClientId, authorizationConsent.principalName)
    }

    override fun findById(registeredClientId: String?, principalName: String?): OAuth2AuthorizationConsent? {
        Assert.notNull(registeredClientId, "registeredClientId cannot be null")
        Assert.notNull(principalName, "principalName cannot be null")

        val authorizationConsent = authorizationConsentRepository.findByRegisteredClientIdAndPrincipalName(registeredClientId!!, principalName!!)
            ?: return null

        return authorizationConsent.toDto()
    }

    fun generateConsent(registeredClientId: String, principalName: String): OAuth2AuthorizationConsent {

        val authorizationConsent = this.findById(registeredClientId, principalName)

        if(authorizationConsent != null) {
            return authorizationConsent
        }


        val newAuthorizationConsent = OAuth2AuthorizationConsent.withId(registeredClientId, principalName)
            .authorities({ it.addAll(setOf(GrantedAuthority { "ROLE_USER" })) })
            .build()

        this.save(newAuthorizationConsent)

        return newAuthorizationConsent
    }

    fun AuthorizationConsent.toDto(): OAuth2AuthorizationConsent {

        clientRepository.findById(this.registeredClientId)
            ?: throw DataRetrievalFailureException("Invalid registeredClientId: ${this.registeredClientId}")

        val authorities = this.authorities.split(",")
            .map { GrantedAuthority { it } }
            .toSet()

        return OAuth2AuthorizationConsent.withId(this.registeredClientId , this.principalName)
            .authorities{ it.addAll(authorities) }
            .build()
    }

    fun OAuth2AuthorizationConsent.toEntity(): AuthorizationConsent {

        val authorities = this.authorities
            .map { it.authority }
            .toSet()
            .joinToString(",")


        return AuthorizationConsent(
            registeredClientId = this.registeredClientId,
            principalName = this.principalName,
            authorities = authorities,
        )
    }
}