package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.domain.SprintDates
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.Tables.ISSUE
import com.kevindeyne.tasker.jooq.Tables.PROJECT
import com.kevindeyne.tasker.jooq.Tables.PROJECT_USERS
import com.kevindeyne.tasker.jooq.Tables.SPRINT
import com.kevindeyne.tasker.jooq.Tables.USER
import com.kevindeyne.tasker.service.SecurityHolder
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.Date
import java.util.stream.Collectors

@Component
open class SprintRepositoryImpl (val dsl: DSLContext, val issueRepository : IssueRepository) : SprintRepository {
	
	val tU = TimeUtils.INSTANCE
		
	override fun findCurrentSprintByProjectId(projectId : Long) : Long {
		val sprintRecord = dsl.selectFrom(PROJECT)
				.where(PROJECT.ID.eq(projectId))					
				.fetchOptional()
		
		if(sprintRecord.isPresent) {
			return sprintRecord.get().get(PROJECT.ACTIVE_SPRINT_ID)
		}
				
		throw RuntimeException("Cannot retrieve sprint when no project is active")
	}
	
	override fun findEndedSprints() : List<Long> {		
		return dsl.select()
         .from(PROJECT)
         .join(SPRINT)
         .on(PROJECT.ACTIVE_SPRINT_ID.eq(SPRINT.ID))
		 .where(SPRINT.END_DATE.lessOrEqual(Timestamp(System.currentTimeMillis())))
		 .fetch()
		 .parallelStream()
		 .map {
			p -> p.get(PROJECT.ID)
		 }
		 .collect(Collectors.toList())				
	}
	
	@Transactional
	override fun startSprint(projectId : Long) : Long {

		val sprintRecord = dsl.selectFrom(PROJECT.join(SPRINT).on(SPRINT.ID.eq(PROJECT.ACTIVE_SPRINT_ID)))
		.where(PROJECT.ID.eq(projectId))			
		.fetchOptional()
		
		if(sprintRecord.isPresent) {
			val currentSprint = sprintRecord.get().get(PROJECT.ACTIVE_SPRINT_ID)
			val sprintLength = sprintRecord.get().get(PROJECT.SPRINT_LENGTH)
			val sprintNr = sprintRecord.get().get(SPRINT.SPRINT_NR)
				
			val newSprintId = createSprint(projectId, 14)
			endSprint(currentSprint)
			setActiveProject(projectId, newSprintId)
			overloadIssue(newSprintId, currentSprint)
			
			val listOfUsers : MutableList<HashMap<String, Any>> = arrayListOf()
			
			dsl.select()
	         .from(PROJECT_USERS)
			 .join(USER)
			 .on(USER.ID.eq(PROJECT_USERS.USER_ID))
			 .where(PROJECT_USERS.PROJECT_ID.eq(projectId))
			 .fetch()
			 .parallelStream()
			 .forEach{
				 p ->
				 val hm = HashMap<String, Any>()
				 hm.put("userId", p.get(USER.ID))
				 hm.put("weeklyWorkload", p.get(USER.WEEKLY_WORKLOAD))
				 listOfUsers.add(hm)
			 }

			for(user in listOfUsers){
				val workloadPerUser = sprintLength*(user.get("weeklyWorkload") as Int)

				var maxWorkload = 0
				
				val issueList = dsl.selectFrom(ISSUE)
				   .where(
					   ISSUE.ASSIGNED.eq(user.get("userId") as Long)
					   .and(ISSUE.STATUS.eq(Progress.BACKLOG.name))
				   )
				   .fetch()
				   .parallelStream()
				   .collect(Collectors.toList())
										
					for(p in issueList) {
					 	maxWorkload += p.get(ISSUE.WORKLOAD)
						println(maxWorkload)
				   	 	if(maxWorkload < workloadPerUser){
		   	 				dsl.update(ISSUE)
								.set(ISSUE.SPRINT_ID, newSprintId)
								.set(ISSUE.STATUS, Progress.IN_SPRINT.name)
								.where(ISSUE.ID.eq(p.get(ISSUE.ID)))
								.execute()
						} else {
							break
						}
					}
			}
					
			val countBacklogIssues = dsl.selectCount()
										.from(Tables.ISSUE)			   
										.where(Tables.ISSUE.PROJECT_ID.eq(projectId))
										.and(Tables.ISSUE.STATUS.eq(Progress.BACKLOG.name).or(Tables.ISSUE.STATUS.eq(Progress.NEW.name)))
										.fetchOne(0, Integer::class.java) as? Int

			dsl.update(SPRINT)
				.set(SPRINT.BACKLOG_AT_START, if (countBacklogIssues != null) countBacklogIssues else 0)
				.set(SPRINT.SPRINT_NR, sprintNr + 1)
				.execute()
			
			SecurityHolder.changeProject(SecurityHolder.getProjectId(), newSprintId)		
			return newSprintId			
		} else {
			throw RuntimeException("Cannot retrieve sprint when no project is active")
		}
	}
	
	fun createSprint(projectId : Long, sprintDays : Int) : Long {
		return dsl.insertInto(SPRINT, SPRINT.START_DATE, SPRINT.END_DATE, SPRINT.PROJECT_ID)
		   .values(Timestamp(System.currentTimeMillis()), tU.inXdays(Date(), sprintDays), projectId)
		   .returning(SPRINT.ID).fetchOne().get(SPRINT.ID)
	}
	
	fun endSprint(sprintId : Long?) {
		dsl.update(SPRINT)
			.set(SPRINT.END_DATE, Timestamp(System.currentTimeMillis()))
			.where(SPRINT.ID.eq(sprintId))		
			.execute()
	}
	
	fun setActiveProject(projectId : Long, sprintId : Long) {
		dsl.update(PROJECT)
			.set(PROJECT.ACTIVE_SPRINT_ID, sprintId)
			.where(PROJECT.ID.eq(projectId))		
			.execute()
	}
	
	fun overloadIssue(newSprintId : Long, currentSprintId : Long?){
		dsl.update(ISSUE)
			.set(ISSUE.SPRINT_ID, newSprintId)
			.set(ISSUE.OVERLOAD, 1)
			.where(ISSUE.SPRINT_ID.eq(currentSprintId))
			.execute()
	}
	
	override fun findSprintEndDate(sprintId : Long) : SprintDates {
		return dsl.select(SPRINT.SPRINT_NR, SPRINT.START_DATE, SPRINT.END_DATE)
	         .from(SPRINT)
			 .where(SPRINT.ID.eq(sprintId))
			 .fetchOne().map {
				s -> SprintDates(s.get(SPRINT.SPRINT_NR), s.get(SPRINT.START_DATE), s.get(SPRINT.END_DATE))
			 }
	}
	
	override fun getBacklogIssuesFromSprintStart(sprintId : Long) : Int {
		return dsl.select(SPRINT.BACKLOG_AT_START)
	         .from(SPRINT)
			 .where(SPRINT.ID.eq(sprintId))
			 .fetchOne().map {
				s -> s.get(SPRINT.BACKLOG_AT_START)
			 }
	}
}