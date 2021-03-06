package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.ProjectForm
import com.kevindeyne.tasker.domain.ProjectListing
import com.kevindeyne.tasker.domain.ProjectVersion

interface ProjectRepository {

	fun findProject(projectId : Long) : ProjectListing
	
	fun findActiveProject(userId : Long) : ProjectListing?
	
	fun findProjects(userId : Long) : List<ProjectListing>
	
	fun changeActiveProject(userId : Long, projectId : Long)
	
	fun createNewProject(userId : Long, form : ProjectForm)
	
	fun getCurrentVersion(projectId : Long) : ProjectVersion

	fun createNewProject(userId : Long, projectTitle: String)
}