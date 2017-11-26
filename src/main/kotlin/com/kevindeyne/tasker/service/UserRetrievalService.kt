package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
open class UserRetrievalService : UserDetailsService {
	
	@Autowired
    lateinit var userRepository: UserRepository
	
	override fun loadUserByUsername(username : String) : UserDetails {
    	var user : UserDetails? = userRepository.findByUsername(username)
        if (user == null) {
            throw UsernameNotFoundException(username)
        }
        return user
    }	
}