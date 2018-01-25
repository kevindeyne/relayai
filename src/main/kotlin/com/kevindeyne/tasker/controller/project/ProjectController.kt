package com.kevindeyne.tasker.controller.project

import com.kevindeyne.tasker.domain.ProjectListing
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
		const val PROJECT_NEW = PROJECT_GET + "/new"
	}
	
	@GetMapping(PROJECT_GET) 
	fun getProjects(model : Model) : String {
		model.addAttribute("showCreatePage", false)
		return genericProjectLayout(model)		
	}
	
	fun genericProjectLayout(model : Model) : String {
		model.addAttribute("activePage", "project");
		
		val projectId : Long = SecurityHolder.getProjectId()
		
		model.addAttribute("currentProject", projectRepository.findProject(projectId))
		model.addAttribute("projects", projectsList())
		
		model.addAttribute("teammembers", userRepository.findTeammembersByProject(projectId))
		model.addAttribute("invites", userRepository.findInvitesByProject(projectId))
		
		return "projects"
	}
	
	fun projectsList() : List<ProjectListing> {
		var result : MutableList<ProjectListing> = mutableListOf()
		result.addAll(projectRepository.findProjects(SecurityHolder.getUserId()))
		result.add(newProject())
		return result
	}
	
	fun newProject() : ProjectListing = ProjectListing(-1L, "Create a new project", "NEW")
	
	@GetMapping(PROJECT_NEW) 
	fun newProject(model : Model) : String {
		model.addAttribute("showCreatePage", true)		
		return genericProjectLayout(model)
	}
}