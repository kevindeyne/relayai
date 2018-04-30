package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class FormResponse @JsonCreator constructor(
		val status: String,
		var element: String = ""
)