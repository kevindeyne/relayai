package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.RegistrationForm
import com.kevindeyne.tasker.repositories.ActivationRepository
import com.kevindeyne.tasker.repositories.ProjectRepository
import com.kevindeyne.tasker.repositories.UserRepository
import com.kevindeyne.tasker.service.RegistrationService
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
class RegistrationController(var userRepository: UserRepository, var projectRepository: ProjectRepository, val passwordEncoder : PasswordEncoder, var activationRepository: ActivationRepository) {

	companion object {
		const val _POST = "/registration"
		const val GET_ATTEMPT_ACTIVATION = "/activation-sample"
		const val GET_ACTIVATION_KEY = "/activation/{key}"
	}

	@PostMapping(_POST) @ResponseBody
	fun submitRegistration(@RequestBody form : RegistrationForm) : FormResponse {
		val formResp = RegistrationService(userRepository, projectRepository, passwordEncoder).registerUser(form)
		return formResp
	}

	@GetMapping(GET_ATTEMPT_ACTIVATION)
	fun attemptActivation() : String {
		val userId = SecurityHolder.getUserId()
		activationRepository.registerActivation(userId)
		return "/settings"
	}

	@GetMapping(GET_ACTIVATION_KEY)
	fun checkActivationKey(@PathVariable key : String) : String {
		return "/settings"
	}
}