package com.ardi.oauth2.Repository

import com.ardi.oauth2.entity.UserClient
import com.ardi.oauth2.entity.UserClientId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserClientRepository: JpaRepository<UserClient, UserClientId> {

    fun findByUsers_UserId(userId: String): UserClient?


    fun findByRegisteredClientId(registeredClientId: String): UserClient?

}