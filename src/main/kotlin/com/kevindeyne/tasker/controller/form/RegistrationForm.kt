package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class RegistrationForm @JsonCreator constructor(
		val username: String,
		val email: String,
		var password: String,
		val password2: String,
		val country: String
)
 