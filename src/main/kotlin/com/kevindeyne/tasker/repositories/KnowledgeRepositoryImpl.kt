package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.KnowledgeRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

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
		return null;
	}
	
	fun addCouplingBetweenTagAndUser(tagId : Long, userId : Long) = dsl.insertInto(Tables.KNOWLEDGE, Tables.KNOWLEDGE.TAG_ID, Tables.KNOWLEDGE.USER_ID).values(tagId, userId).execute();
	
}