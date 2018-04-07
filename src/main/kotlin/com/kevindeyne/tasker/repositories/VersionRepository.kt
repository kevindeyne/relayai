package com.kevindeyne.tasker.repositories

interface VersionRepository {

	fun findTop10MostRecentVersionsInProject(projectId : Long): List<String>

}