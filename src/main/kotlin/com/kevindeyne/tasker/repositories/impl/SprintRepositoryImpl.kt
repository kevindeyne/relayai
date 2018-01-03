package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.jooq.Tables
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.Date
import java.util.stream.Collectors

@Component
open class SprintRepositoryImpl (val dsl: DSLContext) : SprintRepository {
	
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
		val currentSprint = findCurrentSprintByProjectId(projectId)
		val newSprintId = createSprint(projectId, 14)
		endSprint(currentSprint)
		setActiveProject(projectId, newSprintId)
		overloadIssue(newSprintId, currentSprint)		
		return newSprintId
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
}