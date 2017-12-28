package com.kevindeyne.tasker.repositories

import java.util.LinkedList
import com.kevindeyne.tasker.domain.CommentListing

interface CommentRepository {
	fun getCommentsForIssue(issueId: Long) : List<CommentListing>
	
	fun createComment(text : String, issueId : Long, userId : Long) : CommentListing
}