package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.UsersRepository
import com.ardi.oauth2.dto.UsersDTO
import kotlinx.coroutines.supervisorScope
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.stereotype.Service

@Service
final class UserService(
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val users = usersRepository.findByUserId(username)
            ?: throw IllegalArgumentException("username is not found")

        val userDetails = users.toUserDetails()

        InMemoryUserDetailsManager().createUser(userDetails)

        return userDetails
    }

    suspend fun existByUserId(userId: String): Boolean = supervisorScope {
        usersRepository.existsByUserId(userId)
    }

    suspend fun existByEmail(email: String): Boolean = supervisorScope {
        usersRepository.existsByEmail(email)
    }

    suspend fun save(usersDto: UsersDTO): UsersDTO = supervisorScope {

        val encodedPassword = passwordEncoder.encode(usersDto.pwd)
        val newUsersDto = UsersDTO(
            userId = usersDto.userId,
            pwd    = encodedPassword,
            name   = usersDto.name,
            email  = usersDto.email
        )

        val users = usersRepository.save(newUsersDto.toEntity())

        users.toDto()
    }
}