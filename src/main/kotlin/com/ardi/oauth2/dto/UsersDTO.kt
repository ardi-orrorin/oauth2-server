package com.ardi.oauth2.dto

import com.ardi.oauth2.entity.UserRole
import com.ardi.oauth2.entity.Users
import org.springframework.security.core.GrantedAuthority
import java.time.LocalDateTime

data class UsersDTO (
    val id: Long = 0,

    val userId: String,

    val pwd: String,

    val email: String = "",

    val name: String = "",

    val birthDay: String? = null,

    val phone: String? = null,

    val address: String? = null,

    val detailAddress: String? = null,

    val profileImage: String? = null,

    val createdAt: LocalDateTime? = null,

    val updatedAt: LocalDateTime? = null,

    val roles: List<GrantedAuthority> = mutableListOf()
) {
    fun toEntity() = Users(
        id            = this.id,
        userId        = this.userId,
        pwd           = this.pwd,
        email         = this.email,
        name          = this.name,
        birthDay      = this.birthDay,
        phone         = this.phone,
        address       = this.address,
        detailAddress = this.detailAddress,
        profileImage  = this.profileImage,
        createdAt     = this.createdAt ?: LocalDateTime.now(),
        updatedAt     = this.updatedAt ?: LocalDateTime.now(),
        roles         = this.roles.map { UserRole(role = it.authority) }
    )
}
