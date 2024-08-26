package com.ardi.oauth2.entity

import jakarta.persistence.*

@Entity(name = "user_clients")
@Table(name = "user_clients")
@IdClass(UserClientId::class)
data class UserClient(

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", )
    val users: Users,

    @Id
    @ManyToOne
    @JoinColumn(name = "registered_client_id")
    val registeredClient: Client,
){}