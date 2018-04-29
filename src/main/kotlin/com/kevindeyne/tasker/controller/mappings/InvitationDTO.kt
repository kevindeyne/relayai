package com.kevindeyne.tasker.controller.mappings

import com.fasterxml.jackson.annotation.JsonCreator

data class InvitationDTO @JsonCreator constructor(
		var invitorName : String,
		var projectName : String
)