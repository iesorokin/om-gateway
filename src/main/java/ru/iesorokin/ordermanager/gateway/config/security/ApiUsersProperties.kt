package ru.iesorokin.ordermanager.gateway.config.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("api")
class ApiUsersProperties {
    lateinit var users: List<ApiUser>
}

class ApiUser {
    lateinit var username: String
    lateinit var password: String
    lateinit var roles: Set<String>
}