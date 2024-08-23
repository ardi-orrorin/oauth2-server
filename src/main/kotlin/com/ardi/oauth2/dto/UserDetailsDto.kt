package com.ardi.oauth2.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

class UserDetailsDto (
    val id: Long = 0,

    val userId: String,

    val pwd: String,
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return this.pwd
    }

    override fun getUsername(): String {
        return this.userId
    }

    override fun toString(): String {
        return "UserDetailsDto(id=$id, userId='$userId', pwd='$pwd')"
    }

}