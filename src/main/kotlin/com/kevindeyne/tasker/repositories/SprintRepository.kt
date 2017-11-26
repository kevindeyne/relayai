package com.kevindeyne.tasker.repositories

interface SprintRepository {

	fun findCurrentSprintByProjectId(projectId : Long?) : Long?
	
}