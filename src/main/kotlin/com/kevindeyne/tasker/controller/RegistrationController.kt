package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.AcceptInviteForm
import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.RegistrationForm
import com.kevindeyne.tasker.repositories.ActivationRepository
import com.kevindeyne.tasker.repositories.InvitationRepository
import com.kevindeyne.tasker.repositories.UserRepository
import com.kevindeyne.tasker.service.RegistrationService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class RegistrationController(val userRepository: UserRepository, val activationRepository: ActivationRepository, val invitationRepository : InvitationRepository) {

	companion object {
		const val POST = "/registration"
		const val POST_INVITE = "/accept-invite/{inviteID}/{inviteKey}"
	}

	@PostMapping(POST) @ResponseBody
	fun submitRegistration(@RequestBody form : RegistrationForm) : FormResponse {
		return RegistrationService(userRepository, activationRepository, invitationRepository).registerUser(form)
	}

	@PostMapping(POST_INVITE) @ResponseBody
	fun acceptInvite(req : HttpServletRequest, @RequestBody form : AcceptInviteForm, @PathVariable inviteID : String, @PathVariable inviteKey : String) : FormResponse {
		return RegistrationService(userRepository, activationRepository, invitationRepository).acceptInvite(inviteID, inviteKey, form)
	}
}