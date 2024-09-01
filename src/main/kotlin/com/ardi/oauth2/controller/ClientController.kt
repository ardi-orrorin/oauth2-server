package com.ardi.oauth2.controller

import com.ardi.oauth2.dto.UserDetailsDto
import com.ardi.oauth2.dto.request.RegisteredClientRequest
import com.ardi.oauth2.service.ClientInfosService
import com.ardi.oauth2.service.RegisteredClientService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/client")
class ClientController(
    private val clientService: RegisteredClientService,
    private val clientInfosService: ClientInfosService,
) {

    @GetMapping("list")
    suspend fun getClientList(
        @AuthenticationPrincipal principal: UserDetailsDto,
        model: Model,
    ): String = coroutineScope {

        val client = clientService.findAllToDto()
        val clientInfos = clientInfosService.findAllByUserId(principal.username)

        client.map {
            val clientInfo = clientInfos.find { info -> info.clientId == it.clientId }
            it.clientSecret = clientInfo?.clientSecret ?: ""
        }

        model.addAttribute("clients", client)
        "client/list"
    }

    @GetMapping("/detail/{clientId}")
    suspend fun getClientDetail(
        @AuthenticationPrincipal principal: UserDetailsDto,
        @PathVariable clientId: String,
        model: Model,
    ): String = coroutineScope {

        val client = clientService.findInfoByClientId(clientId)

        model.addAttribute("client", client)

        "client/detail"
    }

    @GetMapping("/reset-secret/{clientId}")
    suspend fun resetClientSecret(
        @AuthenticationPrincipal principal: UserDetailsDto,
        @PathVariable clientId: String,
        model: Model,
    ): String = runBlocking {

        clientService.resetClientSecret(clientId, principal.username)

        "redirect:/client/detail/$clientId"
    }

    @GetMapping("/registration")
    suspend fun getClientRegistration(model: Model): String = coroutineScope {
        "client/regist"
    }

    @PostMapping("/registration")
    suspend fun postClientRegistration(
        @AuthenticationPrincipal principal: UserDetailsDto,
        request: RegisteredClientRequest.Create
    ): String = runBlocking {

        request.scopes.contains("openid").let {
            if(!it) request.scopes.add("openid")
        }

        clientService.save(request, principal.username)

        "redirect:/client/list"
    }

    @PutMapping("/detail/{clientId}")
    suspend fun putClientDetail(
        @AuthenticationPrincipal principal: UserDetailsDto,
        @PathVariable clientId: String,
        request: RegisteredClientRequest.Create,
        model: Model,
    ): String = coroutineScope {

        clientService.update(clientId, principal.username, request)

        "redirect:/client/list"
    }

    @DeleteMapping("/delete/{clientId}")
    suspend fun deleteClient(
        @AuthenticationPrincipal principal: UserDetailsDto,
        @PathVariable clientId: String,
        model: Model,
    ): String = coroutineScope {

        clientService.delete(clientId, principal.username)

        "client/list"
    }

}