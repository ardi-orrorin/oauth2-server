package com.ardi.oauth2.entity

import com.ardi.oauth2.dto.ClientDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime

@Entity(name = "client")
@Table(name = "client")
class Client(

    @field:Id
    @field:Column(name= "id")
    val id: String,

    @field:Size(max = 255)
    @field:NotNull
    @Column(name = "client_id", nullable = false)
    val clientId: String,

    @field:Column(name = "client_id_issued_at")
    val clientIdIssuedAt: Instant = Instant.now(),

    @field:Size(max = 255)
    @field:Column(name = "client_secret", nullable = false)
    var clientSecret: String,

    @field:Column(name = "client_secret_expires_at")
    val clientSecretExpiresAt: LocalDateTime? = null,

    @field:Size(max = 255)
    @field:NotNull
    @field:Column(name = "client_name", nullable = false)
    var clientName: String,

    @field:Size(max = 1000)
    @field:NotNull
    @field:Column(name = "client_authentication_methods", nullable = false, length = 1000)
    val clientAuthenticationMethods: String,

    @field:Size(max = 1000)
    @field:NotNull
    @field:Column(name = "authorization_grant_types", nullable = false, length = 1000)
    val authorizationGrantTypes: String,

    @field:Size(max = 1000)
    @field:Column(name = "redirect_uris", length = 1000)
    var redirectUris: String,

    @field:Size(max = 1000)
    @field:Column(name = "post_logout_redirect_uris", length = 1000)
    val postLogoutRedirectUris: String? = null,

    @field:Size(max = 1000)
    @field:NotNull
    @field:Column(name = "scopes", nullable = false, length = 1000)
    var scopes: String,

    @field:Size(max = 2000)
    @field:NotNull
    @field:Column(name = "client_settings", nullable = false, length = 2000)
    val clientSettings: String,

    @field:Size(max = 2000)
    @field:NotNull
    @field:Column(name = "token_settings", nullable = false, length = 2000)
    val tokenSettings: String,

    @OneToMany(mappedBy = "registeredClient")
    val userClients: List<UserClient> = mutableListOf()
) {

    override fun toString(): String {
        return "Client(id='$id', clientId='$clientId', clientIdIssuedAt=$clientIdIssuedAt, clientSecret='$clientSecret', clientSecretExpiresAt=$clientSecretExpiresAt, clientName='$clientName', clientAuthenticationMethods='$clientAuthenticationMethods', authorizationGrantTypes='$authorizationGrantTypes', redirectUris='$redirectUris', postLogoutRedirectUris=$postLogoutRedirectUris, scopes='$scopes', clientSettings='$clientSettings', tokenSettings='$tokenSettings')"
    }

    fun toDto() = ClientDto(
        id = this.id,
        clientId = this.clientId,
        clientIdIssuedAt = this.clientIdIssuedAt,
        clientSecret = this.clientSecret,
        clientSecretExpiresAt = this.clientSecretExpiresAt,
        clientName = this.clientName,
        clientAuthenticationMethods = this.clientAuthenticationMethods,
        authorizationGrantTypes = this.authorizationGrantTypes,
        redirectUris = this.redirectUris,
        postLogoutRedirectUris = this.postLogoutRedirectUris,
        scopes = this.scopes,
        clientSettings = this.clientSettings,
        tokenSettings = this.tokenSettings,
    )
}