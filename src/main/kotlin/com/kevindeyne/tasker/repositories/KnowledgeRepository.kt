package com.kevindeyne.tasker.repositories

interface KnowledgeRepository {
	fun addToKnowledgeIfNotExists(tagId: Long, userId: Long)
	
	fun findMostSuitedCandidateForIssue(issueId: Long) : Long?
}