package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.domain.Workload
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.SprintRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UndeterminedController(var issueRepository : IssueRepository, var sprintRepository : SprintRepository) {
	
	companion object {
		const val UD_PLAN = "/undetermined/{issueId}/plan/{workload}"
		const val UD_CRITICAL = "/undetermined/{issueId}/critical"
		const val UD_FEEDBACK = "/undetermined/{issueId}/feedback"
	}
		
	@PostMapping(UD_PLAN)
	fun updateUndeterminedPlan(@PathVariable issueId : String, @PathVariable workload : String) {
		issueRepository.updateWorkload(issueId.toLong(), SecurityHolder.getUserId().toLong(), Workload.valueOf(workload))
	}
	
	@PostMapping(UD_CRITICAL)
	fun updateUndeterminedCritical(@PathVariable issueId : String) {
		var sprintId = SecurityHolder.getSprintId() 
		if(sprintId == null){
			sprintId = sprintRepository.startSprint(SecurityHolder.getProjectId().toLong())
		}
		issueRepository.updateCritical(issueId.toLong(), SecurityHolder.getUserId().toLong(), sprintId.toLong())	
	}
	
	@PostMapping(UD_FEEDBACK)
	fun updateUndeterminedFeedback(@PathVariable issueId : String) {
		issueRepository.updateStatus(issueId.toLong(), SecurityHolder.getUserId().toLong(), Progress.WAITING_FOR_FEEDBACK)
	}
}