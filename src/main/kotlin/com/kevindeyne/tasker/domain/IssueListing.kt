package com.kevindeyne.tasker.domain

data class IssueListing constructor(
		val id: Long,
		val title: String,
		val shortDescr: String,
		val longDescr: String,
		val clazz: String = "" 
)  { }