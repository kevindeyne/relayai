package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.domain.CommentListing
import com.kevindeyne.tasker.jooq.Tables
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.util.stream.Collectors

@Component
open class CommentRepositoryImpl (val dsl: DSLContext) : CommentRepository {
	
	val tU : TimeUtils = TimeUtils.INSTANCE

	override fun getCommentsForIssue(issueId: Long) : List<CommentListing> {
		return dsl.selectFrom(Tables.COMMENTS.join(Tables.USER).on(Tables.USER.ID.eq(Tables.COMMENTS.USER_ID)))
			   .where(Tables.COMMENTS.ISSUE_ID.eq(issueId))
			   .orderBy(Tables.COMMENTS.POST_DATE.asc())
			   .fetch()
			   .parallelStream()
			   .map {
				  n -> CommentListing(
					    n.get(Tables.COMMENTS.ID),
					    getName(n.get(Tables.USER.USERNAME), n.get(Tables.USER.EMAIL)),
					    tU.toTimeString(n.get(Tables.COMMENTS.POST_DATE)),
					    n.get(Tables.COMMENTS.MESSAGE))
			   }
			   .collect(Collectors.toList())
	}
	
	override fun getCommentsForIssue(issueId: String, maxCommentId: String) : List<CommentListing> {
		return dsl.selectFrom(Tables.COMMENTS.join(Tables.USER).on(Tables.USER.ID.eq(Tables.COMMENTS.USER_ID)))
			   .where(Tables.COMMENTS.ISSUE_ID.eq(issueId.toLong()))
			   .and(Tables.COMMENTS.ID.gt(maxCommentId.toLong()))
			   .orderBy(Tables.COMMENTS.POST_DATE.asc())
			   .fetch()
			   .parallelStream()
			   .map {
				  n -> CommentListing(
					    n.get(Tables.COMMENTS.ID),
					    getName(n.get(Tables.USER.USERNAME), n.get(Tables.USER.EMAIL)),
					    tU.toTimeString(n.get(Tables.COMMENTS.POST_DATE)),
					    n.get(Tables.COMMENTS.MESSAGE))
			   }
			   .collect(Collectors.toList())
	}
	
	fun getComment(commentId: Long) : CommentListing {		
		return dsl.selectFrom(Tables.COMMENTS.join(Tables.USER).on(Tables.USER.ID.eq(Tables.COMMENTS.USER_ID)))
			   .where(Tables.COMMENTS.ID.eq(commentId))
			   .fetchOne()
			   .map {
				  n -> CommentListing(
					    n.get(Tables.COMMENTS.ID),
					    getName(n.get(Tables.USER.USERNAME), n.get(Tables.USER.EMAIL)),
					    tU.toTimeString(n.get(Tables.COMMENTS.POST_DATE)),
					    n.get(Tables.COMMENTS.MESSAGE))
			   }
	}
	
	fun getName(fullName : String?, email : String) : String = if(fullName == null || "".equals(fullName)) { email } else { fullName } 
	
	override fun createComment(text : String, issueId : Long, userId : Long) : CommentListing {
		val commentId : Long = dsl.insertInto(Tables.COMMENTS,
			Tables.COMMENTS.USER_ID, Tables.COMMENTS.ISSUE_ID, Tables.COMMENTS.MESSAGE, Tables.COMMENTS.POST_DATE)
		   .values(userId, issueId, text, Timestamp(System.currentTimeMillis()))
		   .returning(Tables.COMMENTS.ID).fetchOne().get(Tables.COMMENTS.ID)		
		return getComment(commentId)
	}
}