package com.ardi.oauth2.service

import com.ardi.oauth2.Repository.UsersRepository
import com.ardi.oauth2.dto.CustomScope
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.stereotype.Service


@Service
class OidcUserService(
    private val usersRepository: UsersRepository
) {

    fun loadUserByUsername(username: String, scopes: Set<String>): OidcUserInfo {
        val user = usersRepository.findByUserId(username) ?: throw Exception("User not found")

        val props = mutableMapOf<String, String?>(
            "sub"      to user.id.toString().padStart(15, '0'),
            "username" to user.userId,
            "email"    to user.email,
            "profile"  to user.profileImage,
        )

        scopes.forEach {
            when(it) {
                CustomScope.NAME     -> props["name"] = user.name
                CustomScope.BIRTHDAY -> props["birthday"] = user.birthDay ?: ""
                CustomScope.PHONE    -> props["phone"] = user.phone ?: ""
                CustomScope.ADDRESS  -> {
                    props["address"] = user.address ?: ""
                    props["detail_address"] = user.detailAddress ?: ""
                }
            }
        }

        return OidcUserInfo(props.toMap())
    }
}