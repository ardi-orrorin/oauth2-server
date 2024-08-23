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

    @GetMapping("/success")
    fun loginSuccess(): String {
        return "success"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @PostMapping("/signup")
    suspend fun signup(
        @RequestBody user: UserRequest.Signup
    ): ResponseEntity<UsersDTO> = runBlocking {

        val res = userService.save(user.toDto())

        ResponseEntity.ok(res)
    }

}
