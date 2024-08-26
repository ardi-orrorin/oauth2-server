package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.ClientRepository
import com.ardi.oauth2.Repository.UserClientRepository
import com.ardi.oauth2.Repository.UsersRepository
import com.ardi.oauth2.entity.UserClient
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class UserClientServiceTest {

    @Autowired
    lateinit var userClientService: UserClientService

    @Autowired
    lateinit var clientRepository: ClientRepository

    @Autowired
    lateinit var usersRepository: UsersRepository


    @Test
    fun findByUserId() {
        val entity = userClientService.findByUsers_UserId("ardi")
        println(entity)

    }

    @Test
    fun findByClientId() {
    }

    @Test
    fun save() {

//        val users = usersRepository.findByUserId("admin1") ?: throw Exception("User not found")
//
//
//        val client = clientRepository.findByClientId("anamensis-client") ?: throw Exception("Client not found")
        println("client: $client")
//
//        val userClient = UserClient(
//            users = users.toDto().toEntity(),
//            registeredClient = client,
//        )
//        val new=  userClient.copy(
//            users = users,
//            registeredClient = client,
//        )
//
//        userClientService.save(new)


//        val userClient = UserClient(
//            userId = "ardi",
//            registeredClientId = "ardi-client",
//        )
//
//        userClientService.save(userClient)
    }
}