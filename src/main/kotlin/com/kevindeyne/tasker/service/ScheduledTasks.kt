package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.repositories.SprintRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
open class ScheduledTasks(val sprintRepository : SprintRepository) {
	
	companion object {
		const val HOURLY = "0 0 * * * *"
	}
	
	@Scheduled(cron = HOURLY)
	fun hourlySprintCheck() {
		val projectIds = sprintRepository.findEndedSprints()
		projectIds.forEach{ p -> sprintRepository.startSprint(p) }
	}
	
}