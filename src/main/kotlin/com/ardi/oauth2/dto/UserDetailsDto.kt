package com.ardi.oauth2.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class UserDetailsDto (
    val id: Long = 0,

    val userId: String,

    val pwd: String,

    val authorities: List<GrantedAuthority> = listOf()
): UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = this.pwd

    override fun getUsername(): String = this.userId

    override fun toString(): String {
        return "UserDetailsDto(id=$id, userId='$userId', pwd='$pwd')"
    }



}