package com.ardi.oauth2.entity

import jakarta.persistence.Column
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

class AuthorizationConsentId(
    val registeredClientId: String,
    val principalName: String,
) : Serializable {
    override fun hashCode(): Int = Objects.hash(registeredClientId, principalName)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as AuthorizationConsentId

        return registeredClientId == other.registeredClientId &&
                principalName == other.principalName
    }

    companion object {
        private const val serialVersionUID = -33187057079460345L
    }
}