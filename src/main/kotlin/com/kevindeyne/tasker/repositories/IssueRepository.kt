package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.form.StandupResponse
import com.kevindeyne.tasker.domain.IssueListing

interface IssueRepository {
	fun findAllForUser() : List<IssueListing>
	
	fun findById(issueId : Long) : IssueResponse
	
	fun findHighestPrioForUser() : IssueResponse
	
	fun create(title : String, description : String)
	
	fun findUpdateOnIssues(sprintid : String, maxid : String) : List<IssueResponse> //TODO move to pulling 
	
	fun findStandupIssuesForSprint(sprintid : Long) : List<StandupResponse>
}