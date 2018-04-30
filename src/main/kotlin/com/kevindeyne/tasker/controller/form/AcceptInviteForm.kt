package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class AcceptInviteForm @JsonCreator constructor(
		val username: String,
		var password: String,
		val password2: String,
		val country: String
)
 