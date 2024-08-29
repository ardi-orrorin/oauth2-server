package com.ardi.oauth2.Repository

import com.ardi.oauth2.entity.ClientInfos
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientInfosRepository: JpaRepository<ClientInfos, Long> {

    fun findByClientId(clientId: String): ClientInfos?

    fun findAllByUserId(userId: String): List<ClientInfos>

    fun findByClientIdAndUserId(clientId: String, userId: String): ClientInfos?

    fun findByUserIdAndRegisteredClientId(userId: String, registeredClientId: String): ClientInfos?

}