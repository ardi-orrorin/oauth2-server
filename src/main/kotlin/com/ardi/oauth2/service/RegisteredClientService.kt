package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.ClientRepository
import com.ardi.oauth2.entity.Client
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import java.time.Duration
import java.time.ZoneOffset

@Component
final class RegisteredClientService (
    private val clientRepository: ClientRepository,
): RegisteredClientRepository {

    private val objectMapper = ObjectMapper().apply {
        val classLoader = this::class.java.classLoader
        val securityModules = SecurityJackson2Modules.getModules(classLoader)
        registerModules(securityModules)
        registerModule(OAuth2AuthorizationServerJackson2Module())
    }

    override fun save(registeredClient: RegisteredClient?) {
        Assert.notNull(registeredClient, "registeredClient cannot be null")

        clientRepository.save(toEntity(registeredClient!!))
    }

    override fun findById(id: String?): RegisteredClient {
        Assert.notNull(id, "id cannot be null")
        val client = clientRepository.findById(id!!)
            .orElseThrow { IllegalArgumentException("Invalid id: $id") }
        return client.toDto()

    }

    override fun findByClientId(clientId: String?): RegisteredClient {
        Assert.notNull(clientId, "clientId cannot be null")
        val client = clientRepository.findByClientId(clientId!!)
            ?: throw IllegalArgumentException("Invalid clientId: $clientId")

        val dto = client.toDto()

        return dto
    }

    private final fun toEntity(dto: RegisteredClient): Client {

        val methods = dto.clientAuthenticationMethods.map { it.value }.joinToString(",")
        val grantTypes = dto.authorizationGrantTypes.map { it.value }.joinToString(",")

        return Client(
            id = dto.id,
            clientId = dto.clientId,
            clientSecret = dto.clientSecret ?: "",
            clientSecretExpiresAt = dto.clientSecretExpiresAt?.atZone(ZoneOffset.UTC)?.toLocalDateTime()?.plusYears(3),
            clientName = dto.clientName,
            clientAuthenticationMethods = methods,
            authorizationGrantTypes = grantTypes,
            redirectUris = dto.redirectUris.joinToString(","),
            postLogoutRedirectUris = dto.postLogoutRedirectUris?.joinToString(","),
            scopes = dto.scopes.joinToString(","),
            clientSettings = objectMapper.writeValueAsString(dto.clientSettings.settings),
            tokenSettings = objectMapper.writeValueAsString(dto.tokenSettings.settings),
        )
    }

    private final fun Client.toDto(): RegisteredClient {
        val methods = this.clientAuthenticationMethods.split(",")
            .map { ClientAuthenticationMethod(it) }
            .toSet()

        val grantTypes = authorizationGrantTypes.split(",")
            .map { AuthorizationGrantType(it) }
            .toSet()

        val clientSettings = objectMapper
            .readValue(this.clientSettings, object : TypeReference<Map<String, Any>>(){})
        val tokenSettings = objectMapper
            .readValue(this.tokenSettings, object: TypeReference<Map<String, Any>>(){})

        return RegisteredClient
            .withId(this.id)
            .clientId(clientId)
            .clientIdIssuedAt(clientIdIssuedAt)
            .clientSecret(clientSecret)
            .clientSecretExpiresAt(clientSecretExpiresAt?.toInstant(ZoneOffset.UTC))
            .clientName(clientName)
            .clientAuthenticationMethods { it.addAll(methods) }
            .authorizationGrantTypes { it.addAll(grantTypes) }
            .redirectUris { it.addAll(redirectUris.split(",")) }
            .postLogoutRedirectUris { it.addAll(postLogoutRedirectUris?.split(",") ?: emptyList()) }
            .scopes { it.addAll(scopes.split(",")) }
            .clientSettings(ClientSettings.withSettings(clientSettings).build())
            .tokenSettings(TokenSettings.withSettings(tokenSettings).build())
            .build()
    }
}