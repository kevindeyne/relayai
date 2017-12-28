package com.kevindeyne.tasker.domain

data class CommentListing constructor(
		var id: Long = -1L,
		var username: String = "",
		var date: String = "",
		var text: String = ""
)  { }