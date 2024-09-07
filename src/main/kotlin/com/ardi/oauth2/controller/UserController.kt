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
    suspend fun index(): String = supervisorScope {
        "index"
    }

    @GetMapping("/login")
    suspend fun login(): String = supervisorScope{
        "login"
    }

    @GetMapping("/signup")
    suspend fun signup(): String = supervisorScope {
        "signup"
    }

    @PostMapping("/signup")
    suspend fun save(@ModelAttribute @Valid signup: UserRequest.Signup, model: Model): String = supervisorScope {

        val existUserId = async {
            println("existUserId")
            if(userService.existByUserId(signup.userId)) {
                model.addAttribute("validation", "Id already exist")
                return@async true
            }
            return@async false
        }

        val notEvalPwd = async {
            println("notEvalPwd")
            if(signup.pwd != signup.rePwd) {
                model.addAttribute("validation", "Password not match")
                return@async true
            }
            return@async false
        }

        val existEmail = async {
            println("existEmail")
            if(userService.existByEmail(signup.email)) {
                model.addAttribute("validation", "Email already exist")
                return@async true
            }
            return@async false
        }

        if(notEvalPwd.await() || existUserId.await() ||  existEmail.await()) {
            return@supervisorScope "signup"
        }

        userService.save(signup.toDto())

        "redirect:/login"
    }

}
