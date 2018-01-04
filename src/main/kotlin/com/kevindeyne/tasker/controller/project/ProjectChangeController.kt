package com.kevindeyne.tasker.controller.project

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.repositories.ProjectRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProjectChangeController(val projectRepository : ProjectRepository) {
		
	@PostMapping(ProjectController.PROJECT_CHANGE)
	fun changeProject(@PathVariable projectId : String) : FormResponse {
		val userId = SecurityHolder.getUserId()
		projectRepository.changeActiveProject(userId, projectId.toLong())
		return FormResponse(status = "OK")
	}
}