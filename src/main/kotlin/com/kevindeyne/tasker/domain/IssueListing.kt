package com.kevindeyne.tasker.domain

data class IssueListing constructor(
		var id: Long,
		var title: String,
		var shortDescr: String,
		var longDescr: String
)  { }