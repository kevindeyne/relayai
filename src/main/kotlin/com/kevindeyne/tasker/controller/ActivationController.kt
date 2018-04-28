package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.repositories.ActivationRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ActivationController(var activationRepository: ActivationRepository) {

	companion object {
		const val GET_ACTIVATION_KEY = "/activation/{key}"
	}

	@GetMapping(GET_ACTIVATION_KEY)
	fun checkActivationKey(@PathVariable key : String) : String {
		println("start activation: $key")
		activationRepository.deleteActivation(key)
		println("activation completed - now forwarding to /login?activated")
		return "redirect:/login?activated"
	}

	
}