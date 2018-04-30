package com.kevindeyne.tasker.controller.mappings

import com.fasterxml.jackson.annotation.JsonCreator

data class AcceptInvitationDTO @JsonCreator constructor(
		val email : String,
		val projectID : Long
)