package com.kevindeyne.tasker.domain

enum class Urgency(val text: String) {
	IMMEDIATELY("High priority"), NORMAL("Normal urgency"), MINIMAL("Low urgency")
}