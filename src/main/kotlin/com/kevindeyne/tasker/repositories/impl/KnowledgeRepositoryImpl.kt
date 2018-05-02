package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.KnowledgeRecord
import org.jooq.DSLContext
import org.jooq.Record1
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
open class KnowledgeRepositoryImpl (val dsl: DSLContext) : KnowledgeRepository {

	@Transactional
	override fun addToKnowledgeIfNotExists(tagId: Long, userId: Long){		
		if(findIfTagExists(tagId, userId) == null){
			addCouplingBetweenTagAndUser(tagId, userId)
		}
	}
	
	fun findIfTagExists(tagId: Long, userId: Long) : Long? {
		val record : Optional<KnowledgeRecord>  = dsl.selectFrom(Tables.KNOWLEDGE).where(Tables.KNOWLEDGE.TAG_ID.eq(tagId)).and(Tables.KNOWLEDGE.USER_ID.eq(userId)).fetchOptional()		
		if(record.isPresent){
			return record.get().get(Tables.KNOWLEDGE.ID)
		}		
		return null
	}
	
	fun addCouplingBetweenTagAndUser(tagId : Long, userId : Long) = dsl.insertInto(Tables.KNOWLEDGE, Tables.KNOWLEDGE.TAG_ID, Tables.KNOWLEDGE.USER_ID).values(tagId, userId).execute();
	
	override fun findMostSuitedCandidateForIssue(issueId : Long) : Long? {
			val userIdRecord : Record1<Long>? = dsl.select(Tables.KNOWLEDGE.USER_ID)
				.from(Tables.ISSUE)
					.join(Tables.TAGCLOUD).on(Tables.TAGCLOUD.ISSUE_ID.eq(Tables.ISSUE.ID))
					.join(Tables.KNOWLEDGE).on(Tables.KNOWLEDGE.TAG_ID.eq(Tables.TAGCLOUD.TAG_ID))
					.join(Tables.PROJECT_USERS).on(Tables.PROJECT_USERS.USER_ID.eq(Tables.KNOWLEDGE.USER_ID))
			.where(
					Tables.PROJECT_USERS.PROJECT_ID.eq(Tables.ISSUE.PROJECT_ID)
							.and(Tables.ISSUE.ID.eq(issueId))
			)
			.groupBy(Tables.KNOWLEDGE.USER_ID)
			.orderBy(DSL.count().desc())
			.limit(1)
			.fetchOne()
		
		if(userIdRecord != null) {
			return userIdRecord.value1()
		}
		
		return null
	}

	override fun findMostSuitedCandidateForIssue(issueId : Long, role : Role) : Long? {
		val userIdRecord : Record1<Long>? = dsl.select(Tables.KNOWLEDGE.USER_ID)
				.from(Tables.ISSUE)
				.join(Tables.TAGCLOUD).on(Tables.TAGCLOUD.ISSUE_ID.eq(Tables.ISSUE.ID))
				.join(Tables.KNOWLEDGE).on(Tables.KNOWLEDGE.TAG_ID.eq(Tables.TAGCLOUD.TAG_ID))
				.join(Tables.PROJECT_USERS).on(Tables.PROJECT_USERS.USER_ID.eq(Tables.KNOWLEDGE.USER_ID))
				.join(Tables.USER_ROLE).on(Tables.USER_ROLE.USER_ID.eq(Tables.PROJECT_USERS.USER_ID))
				.where(
					Tables.PROJECT_USERS.PROJECT_ID.eq(Tables.ISSUE.PROJECT_ID)
					.and(Tables.ISSUE.ID.eq(issueId))
					.and(Tables.USER_ROLE.ROLE.eq(role.name))
				)
				.groupBy(Tables.KNOWLEDGE.USER_ID)
				.orderBy(DSL.count().desc())
				.limit(1)
				.fetchOne()

		if(userIdRecord != null) {
			return userIdRecord.value1()
		}

		return null
	}
}