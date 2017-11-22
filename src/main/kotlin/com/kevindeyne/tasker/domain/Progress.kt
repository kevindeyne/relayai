package com.kevindeyne.tasker.domain

enum class Progress(val text: String) {
	NEW("New"),
	BACKLOG("Backlog"),
	IN_SPRINT("In sprint"),
	IN_PROGRESS("In progress"),
	DONE("Done");
}