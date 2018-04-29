package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.InviteForm
import com.kevindeyne.tasker.controller.mappings.InvitationDTO
import com.kevindeyne.tasker.controller.timesheet.Keygen
import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.repositories.InvitationRepository
import org.apache.commons.validator.routines.EmailValidator

open class InvitationService(var invitationRepository: InvitationRepository, val emailService: EmailService) {
		
	fun invite(form : InviteForm, projectId : Long) : FormResponse {
		val v = validate(form)
		if(v.status == "OK"){
			val key = Keygen.INSTANCE.newKey()
			val inviteDTO : InvitationDTO = invitationRepository.create(form.email, key, form.role(), projectId)
			emailService.sendInvitationMail(form.email, inviteDTO.inviteId, key, inviteDTO.invitorName, inviteDTO.projectName)
		}

		return v
    }

	fun validate(form : InviteForm) : FormResponse {
		if(!EmailValidator.getInstance(false).isValid(form.email)){
			return FormResponse("NOK", "email")
		}

		try {
			Role.valueOf(form.userType)
		} catch (e : IllegalArgumentException) {
			return FormResponse("NOK", "userType")
		}

		return FormResponse("OK")
	}
}