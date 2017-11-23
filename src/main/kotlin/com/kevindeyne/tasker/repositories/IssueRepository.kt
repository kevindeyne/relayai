package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.domain.IssueListing
import java.sql.Timestamp

interface IssueRepository {
	fun findAllForUser() : List<IssueListing>
	
	fun findById(issueId : Long) : IssueResponse
	
	fun findHighestPrioForUser() : IssueResponse
	
	fun create(title : String, description : String)
	
	fun findUpdateOnIssues(sprintid : String, maxid : String) : List<IssueResponse>
}