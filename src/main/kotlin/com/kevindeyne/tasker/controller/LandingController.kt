package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.mappings.InvitationDTO
import com.kevindeyne.tasker.repositories.InvitationRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class LandingController(val invitationRepository: InvitationRepository) {

	companion object {
		const val GET = "/welcome"
		const val GET_INVITE = "/invite/{inviteID}/{inviteCode}"
	}

	@GetMapping(GET)
	fun getLanding(model : Model) : String {
		model.addAttribute("isLanding", true)
		return "landing"
	}

	@GetMapping(GET_INVITE)
	fun getInviteLanding(model : Model, @PathVariable inviteID : String, @PathVariable inviteCode : String) : String {
		model.addAttribute("isLanding", false)
		val dto : InvitationDTO = invitationRepository.find(inviteID, inviteCode)
		return "landing-invite"
	}
}