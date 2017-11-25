package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.mappings.PullUpdate
import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PullingController(var issueRepository : IssueRepository) {
	
	companion object {
		const val PULLING_TASKBOARD = "/pull/{sprintid}/{issueid}/{maxid}"
		const val PULLING_OTHER = "/pull/{sprintid}"
	}
	
	fun getCurrentUserId() = 0L
	
	@GetMapping(PULLING_TASKBOARD)
	fun getPullUpdateStandard(@PathVariable sprintid : String, @PathVariable issueid : String, @PathVariable maxid : String) : PullUpdate {
		
		var newIssues : List<IssueResponse> = issueRepository.findUpdateOnIssues(sprintid, maxid)
		return PullUpdate(newIssues);
	}
	
	@GetMapping(PULLING_OTHER)
	fun getPullUpdateButOnlyNotifications(@PathVariable sprintid : String) : PullUpdate {
		return PullUpdate(ArrayList<IssueResponse>());
	}	
}