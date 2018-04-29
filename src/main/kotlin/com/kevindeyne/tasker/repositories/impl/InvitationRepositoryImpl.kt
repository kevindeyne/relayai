package com.kevindeyne.tasker.repositories


import com.kevindeyne.tasker.controller.mappings.InvitationDTO
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
open class InvitationRepositoryImpl (val dsl: DSLContext) : InvitationRepository {
	
	override fun create(email : String, userType : String, projectId: Long) : Long {
		return 0L
	}

	override fun find(inviteID: String, inviteCode: String): InvitationDTO {
		return InvitationDTO("Ceci Bishton", "Test project")
	}
}