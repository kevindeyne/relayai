package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import com.kevindeyne.tasker.service.SecurityHolder

@Controller
class DebugController(var issueRepository : IssueRepository) {
	
	companion object {
		const val DEBUG_GET = "/debug"
		const val DEBUG_GET_DAYOFDEPLOY = "/debug/dayofdeploy"		
	}
	
	@GetMapping(DEBUG_GET)
	fun getDebug() : String {		
		return "debug"
	}
	
	@GetMapping(DEBUG_GET_DAYOFDEPLOY)
	fun getDebugDayOfDeploy() : String {
		val sprintId = SecurityHolder.getSprintId()
		val userId = SecurityHolder.getUserId()
		val projectId = SecurityHolder.getProjectId()
		
		issueRepository.createDeployIssue(sprintId, projectId, userId)
		
		return "redirect:/tasks"
	}	
}