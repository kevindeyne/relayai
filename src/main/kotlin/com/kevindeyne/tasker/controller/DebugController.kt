package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import com.kevindeyne.tasker.service.SecurityHolder
import com.kevindeyne.tasker.repositories.SprintRepository

@Controller
class DebugController(var sprintRepository : SprintRepository) {
	
	companion object {
		const val DEBUG_GET = "/debug"
		const val DEBUG_GET_STARTSPRINT = "/debug/startsprint"		
	}
	
	@GetMapping(DEBUG_GET)
	fun getDebug() : String {
		return "debug"
	}
	
	@GetMapping(DEBUG_GET_STARTSPRINT)
	fun getDebugDayOfStartSprint() : String {
		val projectId = SecurityHolder.getProjectId()		
		sprintRepository.startSprint(projectId)		
		return "redirect:/tasks"
	}	
}