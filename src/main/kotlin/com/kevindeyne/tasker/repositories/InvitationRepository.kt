package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.mappings.InvitationDTO

interface InvitationRepository {

	fun create(email : String, userType : String, projectId: Long) : Long

    fun find(inviteID: String, inviteCode: String): InvitationDTO

}