package com.kevindeyne.tasker.repositories


import com.kevindeyne.tasker.controller.mappings.InvitationDTO
import com.kevindeyne.tasker.domain.Role
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
open class InvitationRepositoryImpl (val dsl: DSLContext) : InvitationRepository {
	
	override fun create(email : String, key : String, userType : Role, projectId: Long) : InvitationDTO {
		return InvitationDTO("Ceci Bishton", "Test project", "123")
	}

	override fun find(inviteID: String, inviteCode: String): InvitationDTO {
		return InvitationDTO("Ceci Bishton", "Test project", "123")
	}
}