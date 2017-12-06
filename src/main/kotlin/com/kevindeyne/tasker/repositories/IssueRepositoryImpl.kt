package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.form.StandupResponse
import com.kevindeyne.tasker.domain.IssueListing
import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.IssueRecord
import com.kevindeyne.tasker.service.SecurityHolder
import org.jooq.DSLContext
import org.jooq.tools.StringUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.stream.Collectors

@Component
open class IssueRepositoryImpl (val dsl: DSLContext) : IssueRepository {
		
	@Transactional
	override fun findAllActiveForUserInCurrentSprint() : List<IssueListing> {
		return dsl.selectFrom(Tables.ISSUE)
			   .where(Tables.ISSUE.ASSIGNED.eq(SecurityHolder.getUserId()))
			   .and(Tables.ISSUE.SPRINT_ID.eq(SecurityHolder.getSprintId()))
			   .and(Tables.ISSUE.STATUS.notEqual(Progress.DONE.name))
			   .orderBy(Tables.ISSUE.CREATE_DATE.desc()) //by importance
			   .fetch()
			   .parallelStream()
			   .map {
				  n -> IssueListing(n.get(Tables.ISSUE.ID),
									n.get(Tables.ISSUE.TITLE),
									abbreviate(n.get(Tables.ISSUE.DESCRIPTION)),
									n.get(Tables.ISSUE.DESCRIPTION))
			   }
			   .collect(Collectors.toList())
	}
	
	@Transactional
	override fun findById(issueId : Long) : IssueResponse? {
		var response : IssueRecord? = dsl.selectFrom(Tables.ISSUE)
			   .where(Tables.ISSUE.ID.eq(issueId).and(Tables.ISSUE.ASSIGNED.eq(SecurityHolder.getUserId())))
			   .fetchOne();		
		if(response != null){
			return response.map {
			      n -> IssueResponse(n.get(Tables.ISSUE.ID),
									 n.get(Tables.ISSUE.TITLE),
									 n.get(Tables.ISSUE.DESCRIPTION))
			   }	
		} else {
			return null
		}	   
	}
	
	@Transactional
	override fun findHighestPrioForUser() : IssueResponse {
		return dsl.selectFrom(Tables.ISSUE)
			   .where(Tables.ISSUE.ASSIGNED.eq(SecurityHolder.getUserId()))
			   .orderBy(Tables.ISSUE.CREATE_DATE.desc()) //by importance
			   .limit(1)
			   .fetchAny()
		   	   .map {
			      n -> IssueResponse(n.get(Tables.ISSUE.ID),
									 n.get(Tables.ISSUE.TITLE),
									 n.get(Tables.ISSUE.DESCRIPTION))
			   }
	}
	
	@Transactional
	override fun create(title : String, description : String, userId : Long, sprintId : Long, projectId : Long, assignedTo : Long) : Long {
		val timestamp = Timestamp(System.currentTimeMillis())
		val createAndUpdateUser = userId.toString()
		
		return dsl.insertInto(Tables.ISSUE,
				Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.ASSIGNED, Tables.ISSUE.SPRINT_ID, Tables.ISSUE.PROJECT_ID,
				Tables.ISSUE.CREATE_USER, Tables.ISSUE.UPDATE_USER, Tables.ISSUE.CREATE_DATE, Tables.ISSUE.UPDATE_DATE)
		   .values(title, description, assignedTo, sprintId, projectId, createAndUpdateUser, createAndUpdateUser, timestamp, timestamp)
		   .returning(Tables.ISSUE.ID).fetchOne().get(Tables.ISSUE.ID);
	}
	
		
	@Transactional
	override fun update(issueId : Long, title : String, description : String, userId : Long) {
		val timestamp = Timestamp(System.currentTimeMillis())
		val updateUser = userId.toString()
		
		dsl.update(Tables.ISSUE)
			.set(Tables.ISSUE.TITLE, title)
			.set(Tables.ISSUE.DESCRIPTION, description)
			.set(Tables.ISSUE.UPDATE_USER, updateUser)
			.set(Tables.ISSUE.UPDATE_DATE, timestamp)
			.execute()
	}
	
	@Transactional
	override fun assign(issueId : Long, userId : Long){
		val timestamp = Timestamp(System.currentTimeMillis())
		val updateUser = userId.toString()
		
		dsl.update(Tables.ISSUE)
			.set(Tables.ISSUE.ASSIGNED, userId)
			.set(Tables.ISSUE.UPDATE_USER, updateUser)
			.set(Tables.ISSUE.UPDATE_DATE, timestamp)
			.execute()
	}
	
	@Transactional
	override fun findUpdateOnIssues(sprintId : Long, maxid : String) : List<IssueResponse> {
		return dsl.selectFrom(Tables.ISSUE)
			   .where(Tables.ISSUE.ASSIGNED.eq(SecurityHolder.getUserId()))
			   .and(Tables.ISSUE.ID.gt(maxid.toLong()))
			   .and(Tables.ISSUE.SPRINT_ID.eq(sprintId))
			   .orderBy(Tables.ISSUE.CREATE_DATE.desc()) //by importance
			   .fetch()
			   .parallelStream()
			   .map {
			      n -> IssueResponse(n.get(Tables.ISSUE.ID),
									 n.get(Tables.ISSUE.TITLE),
									 abbreviate(n.get(Tables.ISSUE.DESCRIPTION)))
			   }
			   .collect(Collectors.toList())		
	}
	
	@Transactional
	override fun findStandupIssuesForSprint(sprintId : Long) : List<StandupResponse> {
		return ArrayList<StandupResponse>();
	}
	
	private fun abbreviate(field : String) : String {
		return (StringUtils.abbreviate(field, 120)).replace(Regex("<[^>]*>"), "")
	}	
}