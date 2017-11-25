package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.domain.IssueListing
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.IssueRecord
import org.jooq.DSLContext
import org.jooq.tools.StringUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.stream.Collectors

@Component
open class SprintRepositoryImpl (val dsl: DSLContext) : SprintRepository {
	
	fun getCurrentUserId() = 0L
		
	@Transactional
	override fun findById(issueId : Long) : IssueResponse {
		var response : IssueRecord? = dsl.selectFrom(Tables.ISSUE)
			   .where(Tables.ISSUE.ID.eq(issueId).and(Tables.ISSUE.ASSIGNED.eq(getCurrentUserId())))
			   .fetchOne();		
		if(response != null){
			return response.map {
			      n -> IssueResponse(n.get(Tables.ISSUE.ID),
									 n.get(Tables.ISSUE.TITLE),
									 n.get(Tables.ISSUE.DESCRIPTION))
			   }	
		} else {
			return IssueResponse(-1L, "", "")
		}	   
	}
	
	@Transactional
	override fun findCurrentSprintId() : Long  = 13546L
	
	@Transactional
	override fun create(title : String, description : String) {
		var timestamp : Timestamp = Timestamp(System.currentTimeMillis());
		dsl.insertInto(Tables.ISSUE, Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.ASSIGNED,
				Tables.ISSUE.CREATE_USER, Tables.ISSUE.UPDATE_USER, Tables.ISSUE.CREATE_DATE, Tables.ISSUE.UPDATE_DATE)
		   .values(title, description, 0L, "0", "0", timestamp, timestamp)
		   .execute();
	}
}