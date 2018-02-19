package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.SprintDates

interface SprintRepository {

	fun findCurrentSprintByProjectId(projectId : Long) : Long
	
	fun startSprint(projectId : Long) : Long
	
	fun getBacklogIssuesFromSprintStart(sprintId : Long) : Int
	
	fun findEndedSprints() : List<Long>
	
	fun findSprintEndDate(sprintId : Long) : SprintDates 	
	
}