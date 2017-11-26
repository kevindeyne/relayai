package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.UserPrincipal

interface UserRepository {
	
	fun findByUsername(username : String) : UserPrincipal? 
	
}