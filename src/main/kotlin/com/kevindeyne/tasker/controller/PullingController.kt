package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.mappings.PullUpdate
import com.kevindeyne.tasker.repositories.CommentRepository
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PullingController(val issueRepository : IssueRepository, val commentRepository : CommentRepository) {
	
	companion object {
		const val PULLING_TASKBOARD = "/pull/{issueid}/{maxid}/{maxcommentid}"
		const val PULLING_OTHER = "/pull"
	}
	
	@GetMapping(PULLING_TASKBOARD)
	fun getPullUpdateStandard(@PathVariable issueid : String, @PathVariable maxid : String, @PathVariable maxcommentid : String) : PullUpdate {
		val sprintId = SecurityHolder.getSprintId()
		if(sprintId == null){ return getPullUpdateButOnlyNotifications(); }
		val newIssues = issueRepository.findUpdateOnIssues(sprintId, maxid)
		val comments = commentRepository.getCommentsForIssue(issueid, maxcommentid)
		return PullUpdate(newIssues, comments);
	}
	
	@GetMapping(PULLING_OTHER)
	fun getPullUpdateButOnlyNotifications() : PullUpdate {
		//println("getPullUpdateButOnlyNotifications")
		return PullUpdate()
	}	
}