package com.ardi.oauth2.dto

import com.ardi.oauth2.entity.Client
import java.time.Instant
import java.time.LocalDateTime

data class ClientDto(

    val id: String,
    val clientId: String,
    val clientIdIssuedAt: Instant = Instant.now(),
    var clientSecret: String,
    val clientSecretExpiresAt: LocalDateTime? = null,
    val clientName: String,
    val clientAuthenticationMethods: String,
    val authorizationGrantTypes: String,
    val redirectUris: String,
    val postLogoutRedirectUris: String? = null,
    val scopes: String,
    val clientSettings: String,
    val tokenSettings: String,
) {
    override fun toString(): String {
        return "Client(id='$id', clientId='$clientId', clientIdIssuedAt=$clientIdIssuedAt, clientSecret='$clientSecret', clientSecretExpiresAt=$clientSecretExpiresAt, clientName='$clientName', clientAuthenticationMethods='$clientAuthenticationMethods', authorizationGrantTypes='$authorizationGrantTypes', redirectUris='$redirectUris', postLogoutRedirectUris=$postLogoutRedirectUris, scopes='$scopes', clientSettings='$clientSettings', tokenSettings='$tokenSettings')"
    }

    fun toEntity() = Client(
        id = this.id,
        clientId = this.clientId,
        clientSecret = this.clientSecret,
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