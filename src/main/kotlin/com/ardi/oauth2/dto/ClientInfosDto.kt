package com.ardi.oauth2.dto

import com.ardi.oauth2.entity.ClientInfos
import java.time.Instant

data class ClientInfosDto(
    val id: Long = 0,
    val userId: String,
    val registeredClientId: String,
    val clientId: String,
    val clientSecret: String,
    val createdAt: Instant = Instant.now(),
) {
    fun toEntity() = ClientInfos(
        id = this.id,
        userId = this.userId,
        registeredClientId = this.registeredClientId,
        clientId = this.clientId,
        clientSecret = this.clientSecret,
        createdAt = this.createdAt,
    )
}