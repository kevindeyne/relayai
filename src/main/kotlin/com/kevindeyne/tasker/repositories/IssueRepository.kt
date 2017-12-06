package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.form.StandupResponse
import com.kevindeyne.tasker.domain.IssueListing

interface IssueRepository {
	fun findAllActiveForUserInCurrentSprint() : List<IssueListing>
	
	fun findById(issueId : Long) : IssueResponse?
	
	fun findHighestPrioForUser() : IssueResponse
	
	fun findUpdateOnIssues(sprintId : Long, maxid : String) : List<IssueResponse> //TODO move to pulling 
	
	fun findStandupIssuesForSprint(sprintId : Long) : List<StandupResponse>
	
	fun create(title : String, description : String, userId : Long, sprintId : Long, projectId : Long, assignedTo : Long) : Long
	
	fun update(issueId : Long, title : String, description : String, userId : Long)
	
	fun assign(issueId : Long, userId : Long)
}