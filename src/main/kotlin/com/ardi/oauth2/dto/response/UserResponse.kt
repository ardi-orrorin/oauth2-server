package com.ardi.oauth2.dto.response

class UserResponse {
    data class Login<T>(
        val user: T,
        val token_type: String,
        val access_token: String,
        val access_token_expires_in: Long,
        val refresh_token: String,
        val refresh_token_expires_in: Long
    )
}