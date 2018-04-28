package com.kevindeyne.tasker.repositories

interface InvitationRepository {

	fun create(email : String, userType : String, projectId: Long) : Long
	
}