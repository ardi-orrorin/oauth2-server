package com.ardi.oauth2.controller

import com.ardi.oauth2.dto.request.RegisteredClientRequest
import com.ardi.oauth2.service.RegisteredClientService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/oauth2/client")
class ClientController(
    private val clientService: RegisteredClientService
) {

    @GetMapping("list")
    fun getClientList(model: Model): String {

        model.addAttribute("clients", clientService.findAll())
        return "oauth2/client/list"
    }

    @GetMapping("/registration")
    fun getClientRegistration(model: Model): String {
        return "oauth2/client/regist"
    }

    @PostMapping("/registration")
    fun postClientRegistration(
        request: RegisteredClientRequest.Create
    ): String {

        clientService.save(request)


        return "oauth2/client/regist"
    }
}