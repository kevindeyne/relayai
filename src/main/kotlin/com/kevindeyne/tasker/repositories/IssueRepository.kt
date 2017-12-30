package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.form.StandupResponse
import com.kevindeyne.tasker.domain.Impact
import com.kevindeyne.tasker.domain.InProgressIssue
import com.kevindeyne.tasker.domain.IssueListing
import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.domain.Urgency
import com.kevindeyne.tasker.domain.Workload

interface IssueRepository {
	
	fun findAllActiveForUserInCurrentSprint() : List<IssueListing>
	
	fun findById(issueId : Long) : IssueResponse?
	
	fun findHighestPrioForUser() : IssueResponse
	
	fun findUpdateOnIssues(sprintId : Long, maxid : String) : List<IssueResponse> //TODO move to pulling
	
	fun findStandupIssuesForSprint(sprintId : Long) : List<StandupResponse>
	
	fun create(title : String, description : String, userId : Long, sprintId : Long, projectId : Long, assignedTo : Long) : Long
	
	fun update(issueId : Long, title : String, description : String, userId : Long)
	
	fun updateStatus(issueId : Long, status : Progress, userId : Long)
	
	fun updateUrgency(issueId : Long, urgency : Urgency, userId : Long)
	
	fun updateImpact(issueId : Long, impact : Impact, userId : Long)
	
	fun updateWorkload(issueId : Long, workload : Workload)
	
	fun updateCritical(issueId : Long, userId : Long, sprintId : Long)
	
	fun assign(issueId : Long, userId : Long)
	
	fun findAllInProgress() : List<InProgressIssue>
	
	fun findAllInProgress(userId : Long?, sprintId : Long?) : List<InProgressIssue>
	
}