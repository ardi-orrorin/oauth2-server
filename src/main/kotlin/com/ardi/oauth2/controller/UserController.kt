package com.ardi.oauth2.controller

import com.ardi.oauth2.dto.request.UserRequest
import com.ardi.oauth2.service.UserService
import jakarta.validation.Valid
import kotlinx.coroutines.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.log

@Controller
class UserController(
    val userService: UserService
) {
    @GetMapping("/")
    suspend fun index(): String = coroutineScope {
        "index"
    }

    @GetMapping("/login")
    suspend fun login(): String = coroutineScope{
        "login"
    }

    @GetMapping("/signup")
    suspend fun signup(): String = coroutineScope {
        "signup"
    }

    @PostMapping("/signup")
    suspend fun save(@ModelAttribute @Valid signup: UserRequest.Signup, model: Model): String = coroutineScope {

        if(userService.existByUserId(signup.userId)) {
            model.addAttribute("validation", "Id already exist")
            return@coroutineScope  "signup"
        }


        if(signup.pwd != signup.rePwd) {
            model.addAttribute("validation", "Password not match")
            return@coroutineScope  "signup"
        }

        if(userService.existByEmail(signup.email)) {
            model.addAttribute("validation", "Email already exist")
            return@coroutineScope  "signup"
        }

        userService.save(signup.toDto())

        "redirect:/login"
    }

}
