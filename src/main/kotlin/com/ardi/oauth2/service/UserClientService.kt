package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.UserClientRepository
import com.ardi.oauth2.entity.UserClient
import org.springframework.stereotype.Service


@Service
class UserClientService(
    private val userClientRepository: UserClientRepository
) {

    fun findByUsers_UserId(userId: String) = userClientRepository.findByUsers_UserId(userId)
        ?: throw Exception("User not found")

    fun findByClientId(clientId: String) = userClientRepository.findByRegisteredClientId(clientId)
        ?: throw Exception("Client not found")

    fun save(userClient: UserClient) = userClientRepository.save(userClient)

}