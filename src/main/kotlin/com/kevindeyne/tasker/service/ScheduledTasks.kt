package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.repositories.ActivationRepository
import com.kevindeyne.tasker.repositories.SprintRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
open class ScheduledTasks(val sprintRepository : SprintRepository, val activationRepository: ActivationRepository) {
	
	companion object {
		const val HOURLY = "0 0 * * * *"
	}
	
	@Scheduled(cron = HOURLY)
	fun hourlySprintCheck() {
		val projectIds = sprintRepository.findEndedSprints()
		projectIds.forEach{ p -> sprintRepository.startSprint(p) }

		activationRepository.deleteActivationOutOfTime()
	}
}