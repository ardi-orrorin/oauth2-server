package com.ardi.oauth2.controller

import com.ardi.oauth2.dto.UsersDTO
import com.ardi.oauth2.dto.request.UserRequest
import com.ardi.oauth2.service.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class UserController(
    val userService: UserService
) {
    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/signup")
    fun signup(): String {
        return "signup"
    }

}
