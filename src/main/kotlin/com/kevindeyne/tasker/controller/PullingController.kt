package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.mappings.PullUpdate
import com.kevindeyne.tasker.repositories.CommentRepository
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
class PullingController(val issueRepository : IssueRepository, val commentRepository : CommentRepository) {
	
	companion object {
		const val PULLING_TASKBOARD = "/pull/{issueid}/{latestupdate}/{maxcommentid}/{listtype}"
		const val PULLING_OTHER = "/pull"
	}
	
	@GetMapping(PULLING_TASKBOARD)
	fun getPullUpdateStandard(@PathVariable issueid : String, @PathVariable latestupdate : String, @PathVariable maxcommentid : String, @PathVariable listtype : String) : PullUpdate {
		val sprintId = SecurityHolder.getSprintId()
		if(sprintId == null){ return getPullUpdateButOnlyNotifications(); }
		
		var newIssues : List<IssueResponse>
		var removeIssues : List<String> = listOf()
				
		val lastUpdateAt = Timestamp(latestupdate.toLong())
		if("issue".equals(listtype)){
			newIssues = issueRepository.findUpdateOnMyIssues(sprintId, SecurityHolder.getUserId(), lastUpdateAt)
			removeIssues = issueRepository.findUpdateOnMyIssuesRemoved(sprintId, SecurityHolder.getUserId(), lastUpdateAt)
		} else if("team".equals(listtype)){
			newIssues = issueRepository.findUpdateOnTeamIssues(sprintId, SecurityHolder.getUserId(), lastUpdateAt)
		} else if("backlog".equals(listtype)){
			newIssues = issueRepository.findUpdateOnBacklog(SecurityHolder.getProjectId(), lastUpdateAt)
		} else {
			return getPullUpdateButOnlyNotifications();
		}
				
		val comments = commentRepository.getCommentsForIssue(issueid, maxcommentid)
		
		val myIssueCounter : Int = issueRepository.counterMyIssue(SecurityHolder.getUserId(), sprintId)
		val sprintCounter : Int = issueRepository.counterSprint(SecurityHolder.getUserId(), sprintId)
		val backlogCounter : Int = issueRepository.counterBacklog(SecurityHolder.getProjectId())
		
		return PullUpdate(newIssues, removeIssues, comments, myIssueCounter, sprintCounter, backlogCounter, "#aside-$listtype-list");
	}
	
	@GetMapping(PULLING_OTHER)
	fun getPullUpdateButOnlyNotifications() : PullUpdate {
		//println("getPullUpdateButOnlyNotifications")
		return PullUpdate()
	}	
}