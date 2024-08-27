package com.ardi.oauth2.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@class")
data class UserDetailsDto @JsonCreator constructor(
    @JsonProperty("id") val id: Long = 0,
    @JsonProperty("username") private val userId: String,
    @JsonProperty("password") private val pwd: String,
    @JsonProperty("accountNonExpired") val accountNonExpired: Boolean = true,
    @JsonProperty("accountNonLocked") val accountNonLocked: Boolean = true,
    @JsonProperty("credentialsNonExpired") val credentialsNonExpired: Boolean = true,
    @JsonProperty("enabled") val enabled: Boolean = true,
): UserDetails {

    var authorities: List<String> = mutableListOf();
    override fun getAuthorities(): Collection<GrantedAuthority> = this.authorities.map { GrantedAuthority { it } }

    override fun getPassword(): String = this.pwd

    override fun getUsername(): String = this.userId
    override fun toString(): String {
        return "UserDetailsDto(id=$id, userId='$userId', pwd='$pwd', authorities=$authorities, accountNonExpired=$accountNonExpired, accountNonLocked=$accountNonLocked, credentialsNonExpired=$credentialsNonExpired, enabled=$enabled)"
    }


}