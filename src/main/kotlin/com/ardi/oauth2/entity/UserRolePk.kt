package com.ardi.oauth2.entity

import java.io.Serializable

data class UserRolePk(
    val userId: Long,
    val role: String,
): Serializable {
    constructor(): this(0, "")
}