package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.RegistrationForm
import com.kevindeyne.tasker.repositories.ProjectRepository
import com.kevindeyne.tasker.repositories.UserRepository
import com.kevindeyne.tasker.service.RegistrationService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RegistrationController(var userRepository: UserRepository, var projectRepository: ProjectRepository, val passwordEncoder : PasswordEncoder) {

	companion object {
		const val _POST = "/registration"
	}

	@PostMapping(_POST) @ResponseBody
	fun submitRegistration(@RequestBody form : RegistrationForm) : FormResponse {
		val formResp = RegistrationService(userRepository, projectRepository, passwordEncoder).registerUser(form)
		return formResp
	}
}