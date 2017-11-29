package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.TagRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.Optional

@Component
open class TagcloudRepositoryImpl (val dsl: DSLContext) : TagcloudRepository {

	@Transactional
	override fun addToIssueIfNotExists(keyword: String, issueId: Long?) {
		if(issueId != null){			
			var tagId : Long? = findIfTagExists(keyword)
			if(tagId == null){ tagId = createTag(keyword) }
			addCouplingBetweenTagAndIssue(tagId, issueId)
		}
	}
	
	fun createTag(keyword : String) : Long = dsl.insertInto(Tables.TAG, Tables.TAG.TAG_).values(keyword).returning(Tables.TAG.ID).fetchOne().get(Tables.TAG.ID)
	
	fun addCouplingBetweenTagAndIssue(tagId : Long, issueId : Long) = dsl.insertInto(Tables.TAGCLOUD, Tables.TAGCLOUD.TAG_ID, Tables.TAGCLOUD.ISSUE_ID).values(tagId, issueId).execute();
	
	fun findIfTagExists(keyword : String) : Long? {
		val record : Optional<TagRecord>  = dsl.selectFrom(Tables.TAG).where(Tables.TAG.TAG_.eq(keyword)).fetchOptional()		
		if(record.isPresent){
			return record.get().get(Tables.TAG.ID)
		}		
		return null;	
	}
}