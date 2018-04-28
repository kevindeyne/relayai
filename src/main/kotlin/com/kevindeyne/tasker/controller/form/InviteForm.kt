package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class InviteForm @JsonCreator constructor(
		val email: String,
		val userType: String
)

 