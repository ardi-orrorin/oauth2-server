package com.ardi.oauth2.controller

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*


@Controller
class CustomErrorController: ErrorController {
    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest, model: Model): String? {

        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        if (status != null) {
            val statusCode = status.toString().toInt()
            model.addAttribute("status", statusCode)
            model.addAttribute("error", HttpStatus.valueOf(statusCode).getReasonPhrase())
        } else {
            model.addAttribute("status", 500)
            model.addAttribute("error", "Internal Server Error")
        }
        model.addAttribute("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI))
        model.addAttribute("timestamp", Date())
        val message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE)
        if (message != null && !message.toString().isEmpty()) {
            model.addAttribute("message", message)
        }
        return "error"
    }

    fun getErrorPath(): String? {
        return "/error"
    }
}