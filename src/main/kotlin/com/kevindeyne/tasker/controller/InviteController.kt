package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.InviteForm
import com.kevindeyne.tasker.repositories.InvitationRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class InviteController(val invitationRepository: InvitationRepository) {
	
	companion object {
		const val INVITE_POST = "/invite"
	}

	@PostMapping(INVITE_POST) @ResponseBody
	fun inviteUserIfValid(@RequestBody inviteform : InviteForm) : FormResponse {
		//TODO validation
		invitationRepository.create(inviteform.email, inviteform.userType, SecurityHolder.getProjectId())
		return FormResponse("OK")
	}
}