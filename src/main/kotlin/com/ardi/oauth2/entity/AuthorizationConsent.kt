package com.ardi.oauth2.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "authorization_consent")
@IdClass(AuthorizationConsentId::class)
data class AuthorizationConsent(
    @Id
    @Column(name = "registered_client_id", nullable = false)
    val registeredClientId: String,

    @Id
    @Column(name = "principal_name", nullable = false)
    val principalName: String,

    @Size(max = 1000)
    @Column(name = "authorities", nullable = false, length = 1000)
    val authorities: String,
) {}
