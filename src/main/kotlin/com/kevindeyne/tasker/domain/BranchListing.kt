package com.kevindeyne.tasker.domain

data class BranchListing constructor(
		val id: Long = 1L,
		val name: String = "Trunk",
		val lastCompletedVersion : String = "v4.0.38.3",
		val currentVersion : String = "v4.0.38.4"
)  {
	
}