package com.ardi.oauth2.dto.request

import com.ardi.oauth2.dto.UsersDTO


class UserRequest {
   data class Login (
        val username: String,
        val password: String
    ){}

    data class Signup(
        val userId : String,
        val pwd    : String,
        val name   : String,
        val email  : String
    ){
        fun toDto() = UsersDTO(
            userId = userId,
            pwd = pwd,
            name = name,
            email = email
        )
    }
}