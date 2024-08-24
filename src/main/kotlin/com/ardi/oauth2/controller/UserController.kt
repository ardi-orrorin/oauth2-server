package com.ardi.oauth2.controller

import com.ardi.oauth2.dto.request.UserRequest
import com.ardi.oauth2.service.UserService
import jakarta.validation.Valid
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import kotlin.math.log

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

    @PostMapping("/signup")
    suspend fun save(@ModelAttribute @Valid signup: UserRequest.Signup, model: Model): String = run {
        if(userService.existByUserId(signup.userId)) {
            model.addAttribute("validation", "Id already exist")
            return "signup"
        }

        if(signup.pwd != signup.rePwd) {
            model.addAttribute("validation", "Password not match")
            return "signup"
        }

        if(userService.existByEmail(signup.email)) {
            model.addAttribute("validation", "Email already exist")
            return "signup"
        }

        userService.save(signup.toDto())

        return "redirect:/login"
    }

}
