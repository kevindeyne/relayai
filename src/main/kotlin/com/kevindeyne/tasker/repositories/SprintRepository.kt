package com.kevindeyne.tasker.repositories

interface SprintRepository {

	fun findCurrentSprintByProjectId(projectId : Long?) : Long?
	
	fun startSprint(projectId : Long) : Long
	
	fun findEndedSprints() : List<Long>	
	
}