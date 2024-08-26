package com.ardi.oauth2.controller

import com.ardi.oauth2.dto.CustomScopeEnum
import com.ardi.oauth2.service.RegisteredClientService
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class OAuth2ConsentController(
    private val clientService: RegisteredClientService,
) {
    @GetMapping("/consent")
    fun showConsentPage(
        model: Model,
        @RequestParam(OAuth2ParameterNames.CLIENT_ID) clientId: String,
        @RequestParam(OAuth2ParameterNames.SCOPE) scope: String,
        @RequestParam(OAuth2ParameterNames.STATE) state: String
    ): String {
        val client: RegisteredClient = clientService.findByClientId(clientId)

        val scopes = scope.split(" ")
            .map { CustomScopeEnum.entries.first { scope -> scope.value == it } }
            .toSet()


        model.addAttribute("clientId", clientId)
        model.addAttribute("clientName", client.clientName)
        model.addAttribute("scopes", scopes)
        model.addAttribute("state", state)

        return "consent"
    }

    @GetMapping("/regist/client")
    fun showClientRegistrationPage(model: Model): String {
        return "regist/client"
    }
}