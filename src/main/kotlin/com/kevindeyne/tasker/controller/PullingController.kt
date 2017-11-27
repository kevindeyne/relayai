package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.mappings.PullUpdate
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PullingController(val issueRepository : IssueRepository) {
	
	companion object {
		const val PULLING_TASKBOARD = "/pull/{issueid}/{maxid}"
		const val PULLING_OTHER = "/pull"
	}
	
	fun getCurrentUserId() = 0L
	
	@GetMapping(PULLING_TASKBOARD)
	fun getPullUpdateStandard(@PathVariable issueid : String, @PathVariable maxid : String) : PullUpdate {
		val sprintId = SecurityHolder.getSprintId()
		if(sprintId == null){ return getPullUpdateButOnlyNotifications(); }
		val newIssues : List<IssueResponse> = issueRepository.findUpdateOnIssues(sprintId, maxid)
		return PullUpdate(newIssues);
	}
	
	@GetMapping(PULLING_OTHER)
	fun getPullUpdateButOnlyNotifications() : PullUpdate {
		println("getPullUpdateButOnlyNotifications")
		return PullUpdate(ArrayList<IssueResponse>());
	}	
}