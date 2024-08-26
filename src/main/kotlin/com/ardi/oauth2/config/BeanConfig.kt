package com.ardi.oauth2.config

import com.ardi.oauth2.dto.UserDetailsDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator

@Configuration
class BeanConfig {

    @Bean
    fun objectMapper() = ObjectMapper().apply {
        registerModules(SecurityJackson2Modules.getModules(this::class.java.classLoader))
        addMixIn(HashMap::class.java, UserDetailsDto::class.java)
        registerModule(OAuth2AuthorizationServerJackson2Module())
    }
}