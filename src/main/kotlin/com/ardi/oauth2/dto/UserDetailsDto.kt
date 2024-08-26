package com.ardi.oauth2.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsDto (
    val id: Long = 0,

    val userId: String,

    val pwd: String,

): UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = mutableListOf()

    override fun getPassword(): String = this.pwd

    override fun getUsername(): String = this.userId

    override fun toString(): String {
        return "UserDetailsDto(id=$id, userId='$userId', pwd='$pwd')"
    }



}