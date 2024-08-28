package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.ClientInfosRepository
import com.ardi.oauth2.Repository.ClientRepository
import com.ardi.oauth2.dto.ClientDto
import com.ardi.oauth2.dto.ClientInfosDto
import com.ardi.oauth2.dto.request.RegisteredClientRequest
import com.ardi.oauth2.entity.Client
import com.ardi.oauth2.entity.ClientInfos
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
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
import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID


@Component
final class RegisteredClientService (
    private val clientRepository: ClientRepository,
    private val clientInfosRepository: ClientInfosRepository,
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder,
): RegisteredClientRepository {

    override fun save(registeredClient: RegisteredClient?) {
        Assert.notNull(registeredClient, "registeredClient cannot be null")

        clientRepository.save(registeredClient!!.toEntity())
    }

    fun save(registeredClient: RegisteredClientRequest.Create, userId: String) {

        val registeredClientId = UUID.randomUUID().toString()
        val secert = passwordEncoder.encode(UUID.randomUUID().toString())

        val client = RegisteredClient
            .withId(registeredClientId)
            .clientIdIssuedAt(Instant.now())
            .clientName(registeredClient.clientName)
            .clientId(registeredClient.clientName)
            .clientSecret(passwordEncoder.encode(secert))
            .clientSecretExpiresAt(null)
            .clientAuthenticationMethods {
                it.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            }
            .authorizationGrantTypes {
                it.add(AuthorizationGrantType.AUTHORIZATION_CODE)
            }
            .redirectUris { it.add(registeredClient.redirectUri) }
//            .scopes { it.addAll(registeredClient.scopes) }
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .tokenSettings(TokenSettings.builder()
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .accessTokenTimeToLive(Duration.ofDays(1))
                .build()
            )
            .build()

        clientRepository.save(client.toEntity())

        val clientInfos = ClientInfos(
            userId = userId,
            registeredClientId = registeredClientId,
            clientId = registeredClient.clientName,
            clientSecret = secert,
            createdAt = Instant.now(),
        )

        clientInfosRepository.save(clientInfos)

    }

        override fun findById(id: String?): RegisteredClient {
        Assert.notNull(id, "id cannot be null")
        val client = clientRepository.findById(id!!)
            .orElseThrow { IllegalArgumentException("Invalid id: $id") }
        return client.toRegisteredClient()

    }

    override fun findByClientId(clientId: String?): RegisteredClient {
        Assert.notNull(clientId, "clientId cannot be null")
        val client = clientRepository.findByClientId(clientId!!)
            ?: throw IllegalArgumentException("Invalid clientId: $clientId")

        return client.toRegisteredClient()
    }

    fun findAll(): List<RegisteredClient> {
        return clientRepository.findAll().map { it.toRegisteredClient() }
    }

    fun findAllToDto(): List<ClientDto> {
        return clientRepository.findAll()
            .map { it.toDto() }
    }


    private final fun RegisteredClient.toEntity(): Client {

        val methods = this.clientAuthenticationMethods.map { it.value }.joinToString(",")
        val grantTypes = this.authorizationGrantTypes.map { it.value }.joinToString(",")

        return Client(
            id = this.id,
            clientId = this.clientId,
            clientSecret = this.clientSecret ?: "",
            clientSecretExpiresAt = this.clientSecretExpiresAt?.atZone(ZoneOffset.UTC)?.toLocalDateTime()?.plusYears(3),
            clientName = this.clientName,
            clientAuthenticationMethods = methods,
            authorizationGrantTypes = grantTypes,
            redirectUris = this.redirectUris.joinToString(","),
            postLogoutRedirectUris = this.postLogoutRedirectUris?.joinToString(","),
            scopes = this.scopes.joinToString(","),
            clientSettings = objectMapper.writeValueAsString(this.clientSettings.settings),
            tokenSettings = objectMapper.writeValueAsString(this.tokenSettings.settings),
        )
    }

    private final fun Client.toRegisteredClient(): RegisteredClient {
        val methods = this.clientAuthenticationMethods.split(",")
            .map { ClientAuthenticationMethod(it) }
            .toSet()

        val grantTypes = this.authorizationGrantTypes.split(",")
            .map { AuthorizationGrantType(it) }
            .toSet()

        val clientSettings = objectMapper
            .readValue(this.clientSettings, object : TypeReference<Map<String, Any>>(){})
        val tokenSettings = objectMapper
            .readValue(this.tokenSettings, object: TypeReference<Map<String, Any>>(){})

        return RegisteredClient
            .withId(this.id)
            .clientId(this.clientId)
            .clientIdIssuedAt(this.clientIdIssuedAt)
            .clientSecret(this.clientSecret)
            .clientSecretExpiresAt(this.clientSecretExpiresAt?.toInstant(ZoneOffset.UTC))
            .clientName(this.clientName)
            .clientAuthenticationMethods { it.addAll(methods) }
            .authorizationGrantTypes { it.addAll(grantTypes) }
            .redirectUris { it.addAll(this.redirectUris.split(",")) }
            .postLogoutRedirectUris { it.addAll(this.postLogoutRedirectUris?.split(",") ?: emptyList()) }
            .scopes { it.addAll(this.scopes.split(",")) }
            .clientSettings(ClientSettings.withSettings(clientSettings).build())
            .tokenSettings(TokenSettings.withSettings(tokenSettings).build())
            .build()
    }
}