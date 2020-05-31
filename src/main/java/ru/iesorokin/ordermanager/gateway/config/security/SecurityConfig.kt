package ru.iesorokin.ordermanager.gateway.config.security

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder

@Configuration
class SecurityConfig(
    private val apiUsersProperties: ApiUsersProperties
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .requestMatchers(EndpointRequest.to("health", "info", "prometheus")).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
            .and().httpBasic()
            .and().csrf().disable()
    }

    // Deprecated NoOpPasswordEncoder uses cause we don't need encoding
    @Suppress("deprecation")
    override fun configure(auth: AuthenticationManagerBuilder) {
        val authConfigurer = auth.inMemoryAuthentication()
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
        // add api users
        apiUsersProperties.users.forEach { usr ->
            authConfigurer
                .withUser(usr.username)
                .password(usr.password)
                .roles(*usr.roles.toTypedArray())
                .and()
        }
    }
}