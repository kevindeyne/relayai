package com.kevindeyne.tasker.domain

data class TeammemberListing constructor(
		var id: Long = -1L,
		var name: String = "",
		var role: String = ""
)  { }