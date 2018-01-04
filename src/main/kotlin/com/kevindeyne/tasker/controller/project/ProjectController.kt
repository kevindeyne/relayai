package com.kevindeyne.tasker.controller.project

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.repositories.ProjectRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ProjectController(val projectRepository : ProjectRepository) {
	
	companion object {
		const val PROJECT_GET = "/project"
		const val PROJECT_CHANGE = PROJECT_GET + "/changeto/{projectId}"
	}
	
	@GetMapping(PROJECT_GET) 
	fun getProjects(model : Model) : String {
		model.addAttribute("activePage", "project");
		
		model.addAttribute("currentProject", projectRepository.findProject(SecurityHolder.getProjectId()));
		model.addAttribute("projects", projectRepository.findProjects(SecurityHolder.getUserId()));
		
		return "projects"
	}
}