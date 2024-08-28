package com.ardi.oauth2.entity

import com.ardi.oauth2.dto.ClientInfosDto
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "client_infos")
data class ClientInfos(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "user_id", nullable = false)
    var userId: String,

    @Column(name = "registered_client_id", nullable = false)
    var registeredClientId: String,

    @Column(name = "client_id", nullable = false)
    var clientId: String,

    @Column(name = "client_secret", nullable = false)
    var clientSecret: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),
) {

    fun toDto() = ClientInfosDto(
        id = this.id,
        userId = this.userId,
        registeredClientId = this.registeredClientId,
        clientId = this.clientId,
        clientSecret = this.clientSecret,
        createdAt = this.createdAt,
    )

}