package com.kevindeyne.tasker.config

import com.kevindeyne.tasker.service.UserRetrievalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {
	
	@Autowired
    lateinit var userDetailsService: UserRetrievalService

	@Autowired
	lateinit var env: Environment

    override fun configure(http : HttpSecurity) {
		permitResources(http)
		forceHTTPS(http)
		.authenticationProvider(authenticationProvider())
		.authorizeRequests()
			.antMatchers("/welcome", "/landing/**", "/login").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.defaultSuccessUrl("/", true)
			.loginPage("/login")
			.permitAll()
			.and()
		.logout()
			.logoutSuccessUrl("/")
			.permitAll()

    }

	fun forceHTTPS(http : HttpSecurity) : HttpSecurity{
		if(env.activeProfiles.contains("prd")){
			http.requiresChannel().anyRequest().requiresSecure()
		}
		return http
	}

	fun permitResources(http : HttpSecurity) {
		http.authorizeRequests().antMatchers("/js/jquery**").permitAll()
		http.authorizeRequests().antMatchers("/stylesheets/**").permitAll()
		http.authorizeRequests().antMatchers("/fonts/**").permitAll()
		http.authorizeRequests().antMatchers("/icons/**").permitAll()
	}

	@Bean
	fun authenticationProvider() : DaoAuthenticationProvider {
		val authProvider = DaoAuthenticationProvider()
		authProvider.setUserDetailsService(userDetailsService)
		authProvider.setPasswordEncoder(encoder())
		return authProvider
	}

	@Bean
	fun encoder() : PasswordEncoder  = BCryptPasswordEncoder(11)
}