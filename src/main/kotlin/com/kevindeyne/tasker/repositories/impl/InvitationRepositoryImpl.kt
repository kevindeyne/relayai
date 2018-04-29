package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.mappings.InvitationDTO
import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.service.SecurityHolder
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
open class InvitationRepositoryImpl (val dsl: DSLContext) : InvitationRepository {
	
	override fun create(email : String, key : String, userType : Role, projectId: Long) : InvitationDTO {
		val validUntil = TimeUtils.INSTANCE.inXdays(Date(), 2)

		val invitationId = dsl.insertInto(Tables.INVITATION,
				Tables.INVITATION.EMAIL, Tables.INVITATION.INVITATION_KEY, Tables.INVITATION.PROJECT_ID, Tables.INVITATION.ROLE, Tables.INVITATION.VALID_UNTIL, Tables.INVITATION.INVITER)
				.values(email, key, projectId, userType.name, validUntil, SecurityHolder.getUserId())
				.returning(Tables.INVITATION.ID)
				.fetchOne().get(Tables.INVITATION.ID)

		val projectName = dsl.select(Tables.PROJECT.TITLE)
								.from(Tables.PROJECT)
								.where(Tables.PROJECT.ID.eq(projectId))
								.fetchOne().value1()

		val inviterName = dsl.select(Tables.USER.USERNAME)
								.from(Tables.USER)
								.where(Tables.USER.ID.eq(SecurityHolder.getUserId()))
								.fetchOne().value1()

		return InvitationDTO(inviterName, projectName, invitationId.toString())
	}

	override fun find(inviteID: String, inviteCode: String): InvitationDTO? {

		val invitationResult = dsl.select(Tables.INVITATION.ID, Tables.INVITATION.PROJECT_ID, Tables.INVITATION.INVITER)
				.from(Tables.INVITATION)
				.where(Tables.INVITATION.ID.eq(inviteID.toLong())
						.and(Tables.INVITATION.INVITATION_KEY.eq(inviteCode))
				).fetchOne() ?: return null

		println(invitationResult)

		val projectName = dsl.select(Tables.PROJECT.TITLE)
								.from(Tables.PROJECT)
								.where(Tables.PROJECT.ID.eq(invitationResult.value2()))
								.fetchOne().value1()

		val inviterName = dsl.select(Tables.USER.USERNAME)
								.from(Tables.USER)
								.where(Tables.USER.ID.eq(invitationResult.value3()))
								.fetchOne().value1()

		return InvitationDTO(inviterName, projectName, invitationResult.value1().toString())
	}
}