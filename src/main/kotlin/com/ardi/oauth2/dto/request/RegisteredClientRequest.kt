package com.ardi.oauth2.dto.request

import org.springframework.web.bind.annotation.RequestParam

class RegisteredClientRequest{
    data class Create(
        @RequestParam val clientName: String,
        @RequestParam val redirectUri: String,
        @RequestParam var scopes: Set<String> = setOf("openid"),
    ) {}
}