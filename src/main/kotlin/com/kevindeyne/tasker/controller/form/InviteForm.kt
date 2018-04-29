package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator
import com.kevindeyne.tasker.domain.Role

data class InviteForm @JsonCreator constructor(
		val email: String,
		val userType: String
) {
    fun role(): Role = Role.valueOf(userType)
}

 