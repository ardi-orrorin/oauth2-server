package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.ClientInfosRepository
import org.springframework.stereotype.Service

@Service
class ClientInfosService(
    private val clientInfosRepository: ClientInfosRepository,
){
    fun findByClientId(clientId: String) = clientInfosRepository.findByClientId(clientId)
        ?: throw IllegalArgumentException("Client not found")

    fun findAllByUserId(userId: String) =
        clientInfosRepository.findAllByUserId(userId)
            .map { it.toDto() }


}