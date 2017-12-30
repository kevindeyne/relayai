package com.kevindeyne.tasker.domain

enum class Workload(val text: String, val hours : Int) {
	HIGH("High", 16), NORMAL("Normal", 4), MINIMAL("Low", 2)
}