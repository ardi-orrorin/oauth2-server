package com.ardi.oauth2.Repository

import com.ardi.oauth2.entity.UserRole
import com.ardi.oauth2.entity.UserRolePk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRoleRepository: JpaRepository<UserRole, UserRolePk> {
}