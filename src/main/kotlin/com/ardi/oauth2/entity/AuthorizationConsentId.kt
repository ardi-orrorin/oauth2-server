package com.ardi.oauth2.entity

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class AuthorizationConsentId(
    val registeredClientId: String,
    val principalName: String,
) : Serializable {
    constructor() : this("", "")
}