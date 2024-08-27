package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.UserRoleRepository
import com.ardi.oauth2.Repository.UsersRepository
import com.ardi.oauth2.entity.UserRole
import com.ardi.oauth2.entity.Users
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class UserServiceTest {

//    @Autowired
//    private lateinit var userService: UserService

    @SpyBean
    lateinit var usersRepository: UsersRepository

    @SpyBean
    lateinit var userRoleRepository: UserRoleRepository


    @Test
    fun loadUserByUsername() {
        val user = usersRepository.findByUserId("admin1") ?: throw Exception("User not found")
        println(user)

    }

    @Test
    fun existByUserId() {
    }

    @Test
    fun existByEmail() {
    }

    @Test
    fun save() {

        val roles = setOf(
            UserRole(role = "ROLE_USER"),
            UserRole(role = "ROLE_ADMIN")
        )


        val user = Users(
            userId = "ardi",
            pwd = "ardi",
            name = "ardi",
            email = "ardi@ardi.com",
        )

        usersRepository.save(user)

        val userRoles = roles.map {
            it.copy(userId = user.id)
        }

        userRoleRepository.saveAll(userRoles)
    }
}