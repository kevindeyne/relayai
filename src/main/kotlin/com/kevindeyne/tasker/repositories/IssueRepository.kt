package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.IssueListing

interface IssueRepository {
	fun findAllForUser(userId : String) : List<IssueListing>
	
	fun findById(issueId : Long) : IssueListing
}