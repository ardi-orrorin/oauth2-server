package com.ardi.oauth2.entity

import jakarta.persistence.*

@Entity(name = "user_roles")
@Table(name = "user_roles")
@IdClass(UserRolePk::class)
data class UserRole(
    @Id
    @Column(name = "user_id", nullable = false)
    val userId: Long = 0,

    @Id
    @Column(name = "role", nullable = false)
    val role: String,
){}