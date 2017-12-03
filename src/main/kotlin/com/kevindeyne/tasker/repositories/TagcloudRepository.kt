package com.kevindeyne.tasker.repositories

interface TagcloudRepository {
	fun addToIssueIfNotExists(keyword: String, issueId: Long?)
	
	fun findByIssues(issueId: Long) : List<Long>
}