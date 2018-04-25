package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.repositories.ActivationRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ActivationController(var activationRepository: ActivationRepository) {

	companion object {
		const val GET_ATTEMPT_ACTIVATION = "/activation-sample"
		const val GET_ACTIVATION_KEY = "/activation/{key}"
	}

	@GetMapping(GET_ATTEMPT_ACTIVATION)
	fun attemptActivation() : String {
		val userId = SecurityHolder.getUserId()
		activationRepository.registerActivation(userId)
		return "/settings"
	}

	@GetMapping(GET_ACTIVATION_KEY)
	fun checkActivationKey(@PathVariable key : String) : String {
		activationRepository.deleteActivation(key)
		return "redirect:/tasks"
	}

	
}