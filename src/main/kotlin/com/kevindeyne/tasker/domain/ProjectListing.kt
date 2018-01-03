package com.kevindeyne.tasker.domain

data class ProjectListing constructor(
		val id: Long,
		val title: String,
		val key : String
)  {
	
	fun fullTitle() : String {
		return "$title ($key)"		
	}
	
}