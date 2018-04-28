package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.InviteForm
import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.repositories.InvitationRepository
import org.apache.commons.validator.routines.EmailValidator

open class InvitationService(var invitationRepository: InvitationRepository, val emailService: EmailService) {
		
	fun invite(form : InviteForm, projectId : Long) : FormResponse {
		val v = validate(form)
		if(v.status == "OK"){
			invitationRepository.create(form.email, form.userType, projectId)
			emailService.sendInvitationMail(form.email)
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