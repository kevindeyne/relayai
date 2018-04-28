package com.kevindeyne.tasker.repositories


import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
open class InvitationRepositoryImpl (val dsl: DSLContext) : InvitationRepository {
	
	override fun create(email : String, userType : String, projectId: Long) : Long {
		return 0L
	}
}