package com.ardi.oauth2.controller

import com.ardi.oauth2.dto.UserDetailsDto
import com.ardi.oauth2.dto.request.RegisteredClientRequest
import com.ardi.oauth2.service.ClientInfosService
import com.ardi.oauth2.service.RegisteredClientService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/client")
class ClientController(
    private val clientService: RegisteredClientService,
    private val clientInfosService: ClientInfosService,
) {

    @GetMapping("list")
    fun getClientList(
        @AuthenticationPrincipal principal: UserDetailsDto,
        model: Model,
    ): String {

        val client = clientService.findAllToDto()
        val clientInfos = clientInfosService.findAllByUserId(principal.username)

        client.map {
            val clientInfo = clientInfos.find { info -> info.clientId == it.clientId }
            it.clientSecret = clientInfo?.clientSecret ?: ""
        }

        model.addAttribute("clients", client)
        return "client/list"
    }

    @GetMapping("/registration")
    fun getClientRegistration(model: Model): String {
        return "client/regist"
    }

    @PostMapping("/registration")
    fun postClientRegistration(
        @AuthenticationPrincipal principal: UserDetailsDto,
        request: RegisteredClientRequest.Create
    ): String {

        clientService.save(request, principal.username)

        return "client/regist"
    }
}