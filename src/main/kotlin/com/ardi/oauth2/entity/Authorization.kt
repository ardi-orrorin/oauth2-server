package com.ardi.oauth2.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant

@Entity
@Table(name = "authorizations")
data class Authorization (

    @Id
    @field:Size(max = 255)
    @Column(name = "id", nullable = false)
    val id: String,

    @field:Size(max = 255)
    @field:NotNull
    @Column(name = "registered_client_id", nullable = false)
    val registeredClientId: String,

    @field:Size(max = 255)
    @field:NotNull
    @Column(name = "principal_name", nullable = false)
    val principalName: String,

    @field:Size(max = 255)
    @field:NotNull
    @Column(name = "authorization_grant_type", nullable = false)
    val authorizationGrantType: String,

    @field:Size(max = 1000)
    @Column(name = "authorized_scopes", length = 1000)
    val authorizedScopes: String? = null,

    @field:Size(max = 4000)
    @Column(name = "attributes", length = 4000)
    val attributes: String? = null,

    @field:Size(max = 500)
    @Column(name = "state", length = 500)
    val state: String? = null,

    @field:Size(max = 4000)
    @Column(name = "authorization_code_value", length = 4000)
    val authorizationCodeValue: String? = null,

    @Column(name = "authorization_code_issued_at")
    val authorizationCodeIssuedAt: Instant? = null,

    @Column(name = "authorization_code_expires_at")
    val authorizationCodeExpiresAt: Instant? = null,

    @field:Size(max = 2000)
    @Column(name = "authorization_code_metadata", length = 2000)
    val authorizationCodeMetadata: String? = null,

    @field:Size(max = 4000)
    @Column(name = "access_token_value", length = 4000)
    val accessTokenValue: String? = null,

    @Column(name = "access_token_issued_at")
    val accessTokenIssuedAt: Instant? = null,

    @Column(name = "access_token_expires_at")
    val accessTokenExpiresAt: Instant? = null,

    @field:Size(max = 2000)
    @Column(name = "access_token_metadata", length = 2000)
    val accessTokenMetadata: String? = null,

    @field:Size(max = 255)
    @Column(name = "access_token_type")
    val accessTokenType: String? = null,

    @field:Size(max = 1000)
    @Column(name = "access_token_scopes", length = 1000)
    val accessTokenScopes: String? = null,

    @field:Size(max = 4000)
    @Column(name = "refresh_token_value", length = 4000)
    val refreshTokenValue: String? = null,

    @Column(name = "refresh_token_issued_at")
    val refreshTokenIssuedAt: Instant? = null,

    @Column(name = "refresh_token_expires_at")
    val refreshTokenExpiresAt: Instant? = null,

    @field:Size(max = 2000)
    @Column(name = "refresh_token_metadata", length = 2000)
    val refreshTokenMetadata: String? = null,

    @field:Size(max = 4000)
    @Column(name = "oidc_id_token_value", length = 4000)
    val oidcIdTokenValue: String? = null,

    @Column(name = "oidc_id_token_issued_at")
    val oidcIdTokenIssuedAt: Instant? = null,

    @Column(name = "oidc_id_token_expires_at")
    val oidcIdTokenExpiresAt: Instant? = null,

    @field:Size(max = 2000)
    @Column(name = "oidc_id_token_metadata", length = 2000)
    val oidcIdTokenMetadata: String? = null,

    @field:Size(max = 2000)
    @Column(name = "oidc_id_token_claims", length = 2000)
    val oidcIdTokenClaims: String? = null,

    @field:Size(max = 4000)
    @Column(name = "user_code_value", length = 4000)
    val userCodeValue: String? = null,

    @Column(name = "user_code_issued_at")
    val userCodeIssuedAt: Instant? = null,

    @Column(name = "user_code_expires_at")
    val userCodeExpiresAt: Instant? = null,

    @field:Size(max = 2000)
    @Column(name = "user_code_metadata", length = 2000)
    val userCodeMetadata: String? = null,

    @field:Size(max = 4000)
    @Column(name = "device_code_value", length = 4000)
    val deviceCodeValue: String? = null,

    @Column(name = "device_code_issued_at")
    val deviceCodeIssuedAt: Instant? = null,

    @Column(name = "device_code_expires_at")
    val deviceCodeExpiresAt: Instant? = null,

    @field:Size(max = 2000)
    @Column(name = "device_code_metadata", length = 2000)
    val deviceCodeMetadata: String? = null,
){

}