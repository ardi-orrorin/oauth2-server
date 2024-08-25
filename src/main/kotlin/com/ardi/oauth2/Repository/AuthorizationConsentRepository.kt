package com.ardi.oauth2.Repository

import com.ardi.oauth2.entity.AuthorizationConsent
import com.ardi.oauth2.entity.AuthorizationConsentId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorizationConsentRepository: JpaRepository<AuthorizationConsent, AuthorizationConsentId> {
    fun findByRegisteredClientIdAndPrincipalName(registeredClientId: String, principalName: String): AuthorizationConsent?
    fun deleteByRegisteredClientIdAndPrincipalName(registeredClientId: String, principalName: String): Unit
}