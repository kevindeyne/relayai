package com.kevindeyne.tasker.controller.project

import com.kevindeyne.tasker.repositories.ProjectRepository
import com.kevindeyne.tasker.repositories.UserRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProjectController(val projectRepository : ProjectRepository, val userRepository : UserRepository) {
	
	companion object {
		const val PROJECT_GET = "/project"
		const val PROJECT_CHANGE = PROJECT_GET + "/changeto/{projectId}"
	}
	
	@GetMapping(PROJECT_GET) 
	fun getProjects(model : Model) : String {
		model.addAttribute("activePage", "project");
		
		val projectId : Long = SecurityHolder.getProjectId()
		
		model.addAttribute("currentProject", projectRepository.findProject(projectId))
		model.addAttribute("projects", projectRepository.findProjects(SecurityHolder.getUserId()))
		
		model.addAttribute("teammembers", userRepository.findTeammembersByProject(projectId))
		model.addAttribute("invites", userRepository.findInvitesByProject(projectId))
		
		
		return "projects"
	}
}