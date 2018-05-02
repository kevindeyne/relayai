package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.Role

interface KnowledgeRepository {
	fun addToKnowledgeIfNotExists(tagId: Long, userId: Long)
	
	fun findMostSuitedCandidateForIssue(issueId: Long) : Long?

	fun findMostSuitedCandidateForIssue(issueId : Long, role : Role) : Long?
}