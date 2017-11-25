package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class StandupResponse @JsonCreator constructor(
		var id: Long,
		var title: String,
		var descr: String
)