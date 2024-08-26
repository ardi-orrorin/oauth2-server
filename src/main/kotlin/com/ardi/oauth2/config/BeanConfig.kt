package com.ardi.oauth2.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module

@Configuration
class BeanConfig {

    @Bean
    fun objectMapper() = ObjectMapper().apply {
        registerModules(SecurityJackson2Modules.getModules(this::class.java.classLoader))
        registerModule(OAuth2AuthorizationServerJackson2Module())
    }
}