package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.domain.UserPrincipal
import com.kevindeyne.tasker.repositories.ActivationRepository
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
open class UserRetrievalService(val userRepository: UserRepository, val issueRepository : IssueRepository, val activationRepository : ActivationRepository) : UserDetailsService {
		
	override fun loadUserByUsername(username : String) : UserDetails {
    	var user : UserPrincipal? = userRepository.findByUsername(username)
		
		if(user != null){
			println("A user found ${user.userId}")
			if(activationRepository.hasActiveActivation(user.userId)){
				println("has active activation, cannot login")
				throw UsernameNotFoundException(username)
			}

			println("has no active activation, can login")
			val issues = issueRepository.findAllInProgress(user.userId, user.sprintId)
			user.trackingIssues.addAll(issues.toMutableList())			
		}
		
        if (user == null) {
			println("No user found with username $username")
            throw UsernameNotFoundException(username)
        }
        return user
    }	
}