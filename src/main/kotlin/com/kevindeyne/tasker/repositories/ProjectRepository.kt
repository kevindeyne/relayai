package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.ProjectListing

interface ProjectRepository {

	fun findProject(projectId : Long) : ProjectListing
	
	fun findActiveProject(userId : Long) : ProjectListing 
	
	fun findProjects(userId : Long) : List<ProjectListing>
	
	fun changeActiveProject(userId : Long, projectId : Long)
	
}