package com.ardi.oauth2.controller

import kotlinx.coroutines.runBlocking
import org.springframework.ui.Model
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionAdviseController {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException, model:Model): String = runBlocking {
        model.addAttribute("validation", ex.bindingResult.fieldError?.defaultMessage)
        "signup"
    }

}