package com.ardi.oauth2.dto.response

class RegisteredClientResponse{
    data class Info(
        val clientId: String,
        val clientName: String,
        val clientSecret: String,
        val redirectUris: String,
        val scopes: MutableSet<String>,
    ) {}
}