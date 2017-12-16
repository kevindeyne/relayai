package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProjectController() {
	
	companion object {
		const val PROJECT_GET = "/project"
	}
	
	@GetMapping(PROJECT_GET)
	fun getProjects(model : Model) : String {
		model.addAttribute("activePage", "project");
		return "projects"
	}	
}