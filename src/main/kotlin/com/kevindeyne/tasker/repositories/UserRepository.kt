package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.TeammemberListing
import com.kevindeyne.tasker.domain.UserPrincipal

interface UserRepository {
	
	fun findByUsername(username : String) : UserPrincipal?
	
	fun findUsernameById(id : String) : String
	
	fun findTeammembersByProject(projectId : Long) : List<TeammemberListing>
	
	fun findInvitesByProject(projectId : Long) : List<TeammemberListing>

	fun create(username : String, email : String, password : String) : Long
	
}