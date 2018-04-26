package com.kevindeyne.tasker.config

import com.kevindeyne.tasker.interceptors.CustomAuthenticationSuccessHandler
import com.kevindeyne.tasker.interceptors.HoneypotAuthenticationFilter
import com.kevindeyne.tasker.service.UserRetrievalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {

	@Autowired
	lateinit var encoder: Encoder
	
	@Autowired
    lateinit var userDetailsService: UserRetrievalService

	@Autowired
	lateinit var env: Environment

    override fun configure(http : HttpSecurity) {
		permitResources(http)
		forceHTTPS(http)

		.authenticationProvider(authenticationProvider())
		.authorizeRequests()
			.antMatchers("/welcome", "/landing/**", "/login**", "/monitoring", "/registration**", "/registration/**", "/activation/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.defaultSuccessUrl("/loginSuccess", true)
			.loginPage("/login")
			.permitAll()
			.and()
		.logout()
			.logoutSuccessUrl("/")
			.permitAll()
		.and()
		.addFilterAt(honeypotAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)
			.headers()
			.contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline'; object-src 'none'; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; media-src 'none'; frame-src 'none'; font-src *; connect-src 'self'")
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

	fun honeypotAuthFilter() : HoneypotAuthenticationFilter {
		val h = HoneypotAuthenticationFilter()
		h.setAuthenticationManager(authenticationManagerBean())
		h.setAuthenticationSuccessHandler(CustomAuthenticationSuccessHandler())
		return h
	}

	@Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
	@Throws(Exception::class)
	override fun authenticationManagerBean(): AuthenticationManager {
		return super.authenticationManagerBean()
	}

	@Bean
	fun authenticationProvider() : DaoAuthenticationProvider {
		val authProvider = DaoAuthenticationProvider()
		authProvider.setUserDetailsService(userDetailsService)
		authProvider.setPasswordEncoder(encoder)
		return authProvider
	}
}