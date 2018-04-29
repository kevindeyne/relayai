package com.kevindeyne.tasker.controller.mappings

import com.fasterxml.jackson.annotation.JsonCreator

data class InvitationDTO @JsonCreator constructor(
		val invitorName : String,
		val projectName : String,
		val inviteId : String
)