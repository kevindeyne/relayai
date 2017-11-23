package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.mappings.PullUpdate
import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
class PullingController(var issueRepository : IssueRepository) {
	
	companion object {
		const val PULLING = "/pull/{sprintid}/{issueid}/{maxid}"
	}
	
	fun getCurrentUserId() = 0L
	
	@GetMapping(PULLING)
	fun getPullUpdate(@PathVariable sprintid : String, @PathVariable issueid : String, @PathVariable maxid : String) : PullUpdate {
		
		var newIssues : List<IssueResponse> = issueRepository.findUpdateOnIssues(sprintid, maxid)
		return PullUpdate(newIssues);
	}	
}