package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.domain.SprintDates
import com.kevindeyne.tasker.jooq.Tables
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.Date
import java.util.stream.Collectors

@Component
open class SprintRepositoryImpl (val dsl: DSLContext, val issueRepository : IssueRepository) : SprintRepository {
	
	val tU = TimeUtils.INSTANCE
		
	override fun findCurrentSprintByProjectId(projectId : Long) : Long {
		val sprintRecord = dsl.selectFrom(Tables.PROJECT)
				.where(Tables.PROJECT.ID.eq(projectId))					
				.fetchOptional()
		
		if(sprintRecord.isPresent) {
			return sprintRecord.get().get(Tables.PROJECT.ACTIVE_SPRINT_ID)
		}
				
		throw RuntimeException("Cannot retrieve sprint when no project is active")
	}
	
	override fun findEndedSprints() : List<Long> {		
		return dsl.select()
         .from(Tables.PROJECT)
         .join(Tables.SPRINT)
         .on(Tables.PROJECT.ACTIVE_SPRINT_ID.eq(Tables.SPRINT.ID))
		 .where(Tables.SPRINT.END_DATE.lessOrEqual(Timestamp(System.currentTimeMillis())))
		 .fetch()
		 .parallelStream()
		 .map {
			p -> p.get(Tables.PROJECT.ID)
		 }
		 .collect(Collectors.toList())				
	}
	
	@Transactional
	override fun startSprint(projectId : Long) : Long {

		val sprintRecord = dsl.selectFrom(Tables.PROJECT)
		.where(Tables.PROJECT.ID.eq(projectId))			
		.fetchOptional()
		
		if(sprintRecord.isPresent) {
			val currentSprint = sprintRecord.get().get(Tables.PROJECT.ACTIVE_SPRINT_ID)
			val sprintLength = sprintRecord.get().get(Tables.PROJECT.SPRINT_LENGTH)
				
			val newSprintId = createSprint(projectId, 14)
			endSprint(currentSprint)
			setActiveProject(projectId, newSprintId)
			overloadIssue(newSprintId, currentSprint)
			
			val listOfUsers : MutableList<HashMap<String, Any>> = arrayListOf()
			
			dsl.select()
	         .from(Tables.PROJECT_USERS)
			 .join(Tables.USER)
			 .on(Tables.USER.ID.eq(Tables.PROJECT_USERS.USER_ID))
			 .where(Tables.PROJECT_USERS.PROJECT_ID.eq(projectId))
			 .fetch()
			 .parallelStream()
			 .forEach{
				 p ->
				 val hm = HashMap<String, Any>()
				 hm.put("userId", p.get(Tables.USER.ID))
				 hm.put("weeklyWorkload", p.get(Tables.USER.WEEKLY_WORKLOAD))
				 listOfUsers.add(hm)
			 }
			
			for(user in listOfUsers){
				val workloadPerUser = sprintLength*(user.get("weeklyWorkload") as Int)
				
				var maxWorkload = 0
				
				val issueList = dsl.selectFrom(Tables.ISSUE)
				   .where(
					   Tables.ISSUE.ASSIGNED.eq(user.get("userId") as Long)
					   .and(Tables.ISSUE.STATUS.eq(Progress.BACKLOG.name))
				   )
				   .orderBy(Tables.ISSUE.IMPORTANCE.desc(), Tables.ISSUE.CREATE_DATE.asc())
				   .fetch()
				   .parallelStream()
				   .collect(Collectors.toList())
						
					for(p in issueList) {
					 	maxWorkload += p.get(Tables.ISSUE.WORKLOAD)
				   	 	if(maxWorkload < workloadPerUser){		
		   	 				dsl.update(Tables.ISSUE)
								.set(Tables.ISSUE.SPRINT_ID, newSprintId)
								.set(Tables.ISSUE.STATUS, Progress.IN_SPRINT.name)
								.where(Tables.ISSUE.ID.eq(p.get(Tables.ISSUE.ID)))
								.execute()
						} else {
							break
						}
					}
			}
		
		return newSprintId
			
		} else {
			throw RuntimeException("Cannot retrieve sprint when no project is active")
		}
	}
	
	fun createSprint(projectId : Long, sprintDays : Int) : Long {
		return dsl.insertInto(Tables.SPRINT, Tables.SPRINT.START_DATE, Tables.SPRINT.END_DATE, Tables.SPRINT.PROJECT_ID)
		   .values(Timestamp(System.currentTimeMillis()), tU.inXdays(Date(), sprintDays), projectId)
		   .returning(Tables.SPRINT.ID).fetchOne().get(Tables.SPRINT.ID)
	}
	
	fun endSprint(sprintId : Long?) {
		dsl.update(Tables.SPRINT)
			.set(Tables.SPRINT.END_DATE, Timestamp(System.currentTimeMillis()))
			.where(Tables.SPRINT.ID.eq(sprintId))		
			.execute()
	}
	
	fun setActiveProject(projectId : Long, sprintId : Long) {
		dsl.update(Tables.PROJECT)
			.set(Tables.PROJECT.ACTIVE_SPRINT_ID, sprintId)
			.where(Tables.PROJECT.ID.eq(projectId))		
			.execute()
	}
	
	fun overloadIssue(newSprintId : Long, currentSprintId : Long?){
		dsl.update(Tables.ISSUE)
			.set(Tables.ISSUE.SPRINT_ID, newSprintId)
			.set(Tables.ISSUE.OVERLOAD, 1)
			.where(Tables.ISSUE.SPRINT_ID.eq(currentSprintId))
			.execute()
	}
	
	override fun findSprintEndDate(sprintId : Long) : SprintDates {
		return dsl.select(Tables.SPRINT.START_DATE, Tables.SPRINT.END_DATE)
	         .from(Tables.SPRINT)
			 .where(Tables.SPRINT.ID.eq(sprintId))
			 .fetchOne().map {
				s -> SprintDates(s.get(Tables.SPRINT.START_DATE), s.get(Tables.SPRINT.END_DATE))
			 }
	}
}