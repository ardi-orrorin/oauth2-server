package com.ardi.oauth2.entity

import com.ardi.oauth2.dto.UserDetailsDto
import com.ardi.oauth2.dto.UsersDTO
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.*
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    val id: Long = 0,

    @field:Size(min = 4, max = 255, message = "userId must be between 4 and 255 characters")
    @Column(name = "user_id", length = 255, nullable = false, unique = true, updatable = false)
    val userId: String,

    @Column(name = "pwd", length = 255, nullable = false)
    val pwd: String,

    @field:Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$", message = "email is not valid")
    @Column(name="email", length = 255, nullable = false, unique = true, updatable = false)
    val email: String = "",

    @field:Size(min = 2, max = 255, message = "name must be between 2 and 255 characters")
    @Column(name="name", length = 255, nullable = false)
    val name: String = "",

    @Column(name="birthday", length = 12, nullable = true)
    val birthDay: String? = null,

    @field:Pattern(regexp = "^010-(\\d{3,4})-(\\d{4})\$", message = "phone is not valid")
    @Column(name = "phone", length = 20, nullable = true, unique = true)
    val phone: String? = null,

    @Column(name = "address", length = 255,  nullable = true)
    val address: String? = null,

    @Column(name = "detail_address", length = 255, nullable = true)
    val detailAddress: String? = null,

    @Column(name = "profile_image", length = 255, nullable = true)
    val profileImage: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "userId", cascade = [CascadeType.PERSIST] , fetch = FetchType.EAGER)
    val roles: List<UserRole> = mutableListOf()
) {

    fun toUserDetails(): UserDetailsDto {
        val user = UserDetailsDto(
            id = id,
            userId = userId,
            pwd = pwd,
        )
        user.authorities = roles.map { it.role }.toMutableList()
        val new = user.copy()

        return new
    }

    fun toDto() = UsersDTO (
        id            = id,
        userId        = userId,
        pwd           = pwd,
        email         = email,
        name          = name,
        birthDay      = birthDay,
        phone         = phone,
        address       = address,
        detailAddress = detailAddress,
        profileImage  = profileImage,
        createdAt     = createdAt,
        updatedAt     = updatedAt,
        roles         = roles.map { GrantedAuthority { it.role } }.toMutableList()
    )

    override fun toString(): String {
        return "Users(id=$id, userId='$userId', pwd='$pwd', email='$email', name='$name', birthDay=$birthDay, phone=$phone, address=$address, detailAddress=$detailAddress, profileImage=$profileImage, createdAt=$createdAt, updatedAt=$updatedAt, roles=$roles)"
    }

}