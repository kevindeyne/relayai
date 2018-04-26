package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.security.crypto.password.PasswordEncoder

data class RegistrationForm @JsonCreator constructor(
		val username: String,
		val email: String,
		var password: String
){
	fun encodePassword(passwordEncoder : PasswordEncoder) {
		this.password = passwordEncoder.encode(this.password)
	}
}

 