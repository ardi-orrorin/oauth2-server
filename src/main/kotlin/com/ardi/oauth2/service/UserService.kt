package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.UsersRepository
import com.ardi.oauth2.dto.UsersDTO
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

    suspend fun existByUserId(userId: String): Boolean {
        return usersRepository.existsByUserId(userId)
    }

    suspend fun existByEmail(email: String): Boolean {
        return usersRepository.existsByEmail(email)
    }

    suspend fun save(usersDto: UsersDTO): UsersDTO {

        val encodedPassword = passwordEncoder.encode(usersDto.pwd)
        val newUsersDto = UsersDTO(
            userId = usersDto.userId,
            pwd    = encodedPassword,
            name   = usersDto.name,
            email  = usersDto.email
        )

        val users = usersRepository.save(newUsersDto.toEntity())

        return users.toDto()
    }
//
//    suspend fun findByUserId(userId: String): UsersDTO {
//        val users = usersRepository.findByUserId(userId)
//            ?: throw IllegalArgumentException("username is not found")
//
//        return users.toDto()
//    }

//    suspend fun login(username: String, password: String): UserResponse.Login<UsersDTO> {
//        val users = usersRepository.findByUserId(username)
//            ?: throw IllegalArgumentException("username is not found")
//
//        if (!passwordEncoder.matches(password, users.pwd)) {
//            throw IllegalArgumentException("password is not correct")
//        }
//
//        TODO("토큰 생성 로직 추가 예정")
//
//        val res = UserResponse.Login(
//            users.toDto(),
//            "Bearer",
//            "access_token",
//            3600,
//            "refresh_token",
//            3600,
//        )
//
//        return res
//    }
}