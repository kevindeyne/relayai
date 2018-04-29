package com.kevindeyne.tasker.controller.timesheet

import java.util.*

enum class Keygen {
	
	INSTANCE;

	fun newKey() = UUID.randomUUID().toString().replace("-", Random().nextInt(9).toString())
}