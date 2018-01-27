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
		
		model.addAttribute("projects", projectsList())
		
		val projectId : Long = SecurityHolder.getProjectId()
		if(SecurityHolder.getProjectId() != -1L){
			model.addAttribute("currentProject", projectRepository.findProject(projectId))
					
			model.addAttribute("teammembers", userRepository.findTeammembersByProject(projectId))
			model.addAttribute("invites", userRepository.findInvitesByProject(projectId))
		} else {
			model.addAttribute("currentProject", ProjectListing(1, "", ""))
		}
				
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
		model.addAttribute("noOtherProject", SecurityHolder.getProjectId() == -1L)	
		return genericProjectLayout(model)
	}
}