package com.ardi.oauth2.dto.request

import com.ardi.oauth2.dto.UsersDTO
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


class UserRequest {
    data class Signup(
        @field:Size(min = 4, message = "User ID must be between 4 and 16 characters")
        val userId  : String,

        @field:Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
        val pwd     : String,

        val rePwd   : String,

        val name    : String,
        @field:Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$", message = "Email is not valid")
        val email   : String
    ){
        fun toDto() = UsersDTO(
            userId = this.userId,
            pwd    = this.pwd,
            name   = this.name,
            email  = this.email
        )
    }
}