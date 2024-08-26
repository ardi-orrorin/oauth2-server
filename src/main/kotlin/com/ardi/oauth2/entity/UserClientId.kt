package com.ardi.oauth2.entity

import java.io.Serializable

data class UserClientId(
    var users: Users,
    var registeredClient: Client,
) : Serializable {
    constructor() : this(
        Users(
            userId = "",
            pwd = "",
            email = "",
            name = "",
            birthDay = "",
        ),
        Client(
            id = "",
            clientId = "",
            clientSecret = "",
            clientName = "",
            clientAuthenticationMethods = "",
            authorizationGrantTypes = "",
            redirectUris = "",
            scopes = "",
            clientSettings = "",
            tokenSettings = "",
        ))
}

