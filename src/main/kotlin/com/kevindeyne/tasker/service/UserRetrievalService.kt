package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.domain.UserPrincipal
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
open class UserRetrievalService(var userRepository: UserRepository, var issueRepository : IssueRepository) : UserDetailsService {
		
	override fun loadUserByUsername(username : String) : UserDetails {
    	var user : UserPrincipal? = userRepository.findByUsername(username)
		
		if(user != null){
			val issues = issueRepository.findAllInProgress(user.userId, user.sprintId)
			user.trackingIssues.addAll(issues.toMutableList())			
		}
		
        if (user == null) {
            throw UsernameNotFoundException(username)
        }
        return user
    }	
}