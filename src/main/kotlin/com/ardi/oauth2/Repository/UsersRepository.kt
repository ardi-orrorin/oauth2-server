package com.ardi.oauth2.Repository

import com.ardi.oauth2.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository: JpaRepository<Users, Long> {

    fun findByUserId(userId: String): Users?
}