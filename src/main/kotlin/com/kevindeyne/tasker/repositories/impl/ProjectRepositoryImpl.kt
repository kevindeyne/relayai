package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.ProjectForm
import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.domain.ProjectListing
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.ProjectRecord
import com.kevindeyne.tasker.service.SecurityHolder
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.Date
import java.util.Optional
import java.util.stream.Collectors
import com.kevindeyne.tasker.domain.SprintFrequency

@Component
open class ProjectRepositoryImpl (val dsl: DSLContext, val sprintRepository : SprintRepository, val issueRepository : IssueRepository) : ProjectRepository {
	
	val tU = TimeUtils.INSTANCE
	
	override fun findProject(projectId : Long) : ProjectListing {
		return dsl.select()
				.from(Tables.PROJECT)
			    .where(Tables.PROJECT.ID.eq(projectId))
			    .fetchOne()
			    .map {
				  n -> ProjectListing(n.get(Tables.PROJECT.ID),
									n.get(Tables.PROJECT.TITLE),
									n.get(Tables.PROJECT.KEY))
			   }
	}
	
	override fun findActiveProject(userId : Long) : ProjectListing? {
		val record = dsl.select()
				.from(Tables.PROJECT)
				.join(Tables.PROJECT_USERS).on(Tables.PROJECT_USERS.PROJECT_ID.eq(Tables.PROJECT.ID))
			    .where(Tables.PROJECT_USERS.USER_ID.eq(userId))
			    .and(Tables.PROJECT_USERS.ACTIVE.eq(true))
			    .fetchOptional()

		if(record.isPresent){
			return record.get().map {
			  n -> ProjectListing(n.get(Tables.PROJECT.ID),
								n.get(Tables.PROJECT.TITLE),
								n.get(Tables.PROJECT.KEY))
		   }
		} else {
			return null
		}    
	}
	
	override fun findProjects(userId : Long) : List<ProjectListing>	 {
		return dsl.select()
				.from(Tables.PROJECT)
				.join(Tables.PROJECT_USERS).on(Tables.PROJECT_USERS.PROJECT_ID.eq(Tables.PROJECT.ID))
			    .where(Tables.PROJECT_USERS.USER_ID.eq(userId))
			    .fetch()
			    .parallelStream()
			    .map {
				  n -> ProjectListing(n.get(Tables.PROJECT.ID),
									n.get(Tables.PROJECT.TITLE),
									n.get(Tables.PROJECT.KEY))
			   }
			   .collect(Collectors.toList())
	}
	
	@Transactional
	override fun changeActiveProject(userId : Long, projectId : Long) {
		updateActiveProjectUser(userId, SecurityHolder.getProjectId(), false)
		updateActiveProjectUser(userId, projectId, true)
		
		val sprintId = sprintRepository.findCurrentSprintByProjectId(projectId)
		SecurityHolder.changeProject(projectId, sprintId)
	}
	
	fun updateActiveProjectUser(userId : Long, projectId : Long, active : Boolean) {
		dsl.update(Tables.PROJECT_USERS)
			.set(Tables.PROJECT_USERS.ACTIVE, active)
			.where(Tables.PROJECT_USERS.USER_ID.eq(userId)).and(Tables.PROJECT_USERS.PROJECT_ID.eq(projectId))
			.execute()
	}
	
	@Transactional
	override fun createNewProject(userId : Long, form : ProjectForm) {
		val title : String = form.title.capitalize()
		val sprintLength : Int = SprintFrequency.valueOf(form.sprintFrequency).weeks
		val projectId = buildProject(userId, title, sprintLength)
		
		setProjectAsActive(projectId, buildOriginSprint(userId, projectId, 2))
		changeActiveProject(userId, projectId)	
	}
	
	fun buildProject(userId : Long, title : String, sprintLength : Int) : Long {
		val key : String = title.substring(3).toUpperCase()
		
		val projectId = dsl.insertInto(Tables.PROJECT,
				Tables.PROJECT.TITLE, Tables.PROJECT.KEY, Tables.PROJECT.SPRINT_LENGTH)
		   .values(title, key, sprintLength)
		   .returning(Tables.PROJECT.ID).fetchOne().get(Tables.PROJECT.ID)
		
		dsl.insertInto(Tables.PROJECT_USERS,
			  Tables.PROJECT_USERS.PROJECT_ID, Tables.PROJECT_USERS.USER_ID, Tables.PROJECT_USERS.ACTIVE)
		   .values(projectId, userId, true)
		   .execute()
		
		return projectId
	}
			
	fun setProjectAsActive(projectId : Long, sprintId : Long) {
		dsl.update(Tables.PROJECT)
			.set(Tables.PROJECT.ACTIVE_SPRINT_ID, sprintId)
			.where(Tables.PROJECT.ID.eq(projectId))		
			.execute()
	}
	
	fun buildOriginSprint(userId : Long, projectId : Long, sprintLength : Int) : Long {
		
		val sprintId : Long = dsl.insertInto(Tables.SPRINT, Tables.SPRINT.START_DATE, Tables.SPRINT.END_DATE, Tables.SPRINT.PROJECT_ID)
		   .values(Timestamp(System.currentTimeMillis()), tU.inXdays(Date(), sprintLength*7), projectId)
		   .returning(Tables.SPRINT.ID).fetchOne().get(Tables.SPRINT.ID)
		
		issueRepository.createInProgress("Invite teammembers", "Please take the time to invite any other teammembers to your projects. This can be done via the project screen.", userId, sprintId, projectId, userId)
		issueRepository.createInProgress("Invite shareholders", "Please take the time to invite any shareholders to your projects. This can be done via the project screen.", userId, sprintId, projectId, userId)
				
		return sprintId		
	}
}