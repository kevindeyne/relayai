package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.domain.IssueListing

interface IssueRepository {
	fun findAllForUser() : List<IssueListing>
	
	fun findById(issueId : Long) : IssueResponse
	
	fun findHighestPrioForUser() : IssueResponse	
}