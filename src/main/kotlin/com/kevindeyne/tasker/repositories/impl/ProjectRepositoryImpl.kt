package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.ProjectForm
import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.domain.ProjectListing
import com.kevindeyne.tasker.domain.ProjectVersion
import com.kevindeyne.tasker.domain.SprintFrequency
import com.kevindeyne.tasker.jooq.Tables.PROJECT
import com.kevindeyne.tasker.jooq.Tables.PROJECT_USERS
import com.kevindeyne.tasker.jooq.Tables.SPRINT
import com.kevindeyne.tasker.service.SecurityHolder
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.Date
import java.util.stream.Collectors

@Component
open class ProjectRepositoryImpl (val dsl: DSLContext, val sprintRepository : SprintRepository, val issueRepository : IssueRepository) : ProjectRepository {
	
	val tU = TimeUtils.INSTANCE
	val maxExpectedProjects = 10
	
	override fun findProject(projectId : Long) : ProjectListing {
		return dsl.select(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY)
				.from(PROJECT)
			    .where(PROJECT.ID.eq(projectId))
			    .fetchOne()
			    .map {
				  n -> ProjectListing(n.get(PROJECT.ID),
									n.get(PROJECT.TITLE),
									n.get(PROJECT.KEY))
			   }
	}
	
	override fun findActiveProject(userId : Long) : ProjectListing? {
		val record = dsl.select(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY)
				.from(PROJECT)
				.join(PROJECT_USERS).on(PROJECT_USERS.PROJECT_ID.eq(PROJECT.ID))
			    .where(PROJECT_USERS.USER_ID.eq(userId))
			    .and(PROJECT_USERS.ACTIVE.eq(true))
			    .fetchOptional()

		if(record.isPresent){
			return record.get().map {
			  n -> ProjectListing(n.get(PROJECT.ID),
								n.get(PROJECT.TITLE),
								n.get(PROJECT.KEY))
		   }
		} else {
			return null
		}
	}
	
	override fun findProjects(userId : Long) : List<ProjectListing>	 {
		return dsl.select(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY)
				.from(PROJECT)
				.join(PROJECT_USERS).on(PROJECT_USERS.PROJECT_ID.eq(PROJECT.ID))
			    .where(PROJECT_USERS.USER_ID.eq(userId))
				.limit(maxExpectedProjects)
			    .fetch()
			    .parallelStream()
			    .map {
				  p -> ProjectListing(p.get(PROJECT.ID),
									p.get(PROJECT.TITLE),
									p.get(PROJECT.KEY))
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
		dsl.update(PROJECT_USERS)
			.set(PROJECT_USERS.ACTIVE, active)
			.where(PROJECT_USERS.USER_ID.eq(userId)).and(PROJECT_USERS.PROJECT_ID.eq(projectId))
			.execute()
	}
	
	@Transactional
	override fun createNewProject(userId : Long, form : ProjectForm) {
		val title : String = form.title.capitalize()
		val sprintLength : Int = SprintFrequency.valueOf(form.sprintFrequency.toUpperCase()).weeks
		val projectId = buildProject(userId, title, sprintLength)
		
		setProjectAsActive(projectId, buildOriginSprint(userId, projectId, 2))
		changeActiveProject(userId, projectId)	
	}
	
	fun buildProject(userId : Long, title : String, sprintLength : Int) : Long {
		val key : String = keyGeneration(title)
		
		val projectId = dsl.insertInto(PROJECT,
				PROJECT.TITLE, PROJECT.KEY, PROJECT.SPRINT_LENGTH)
		   .values(title, key, sprintLength)
		   .returning(PROJECT.ID).fetchOne().get(PROJECT.ID)
		
		dsl.insertInto(PROJECT_USERS,
			  PROJECT_USERS.PROJECT_ID, PROJECT_USERS.USER_ID, PROJECT_USERS.ACTIVE)
		   .values(projectId, userId, true)
		   .execute()
		
		return projectId
	}
	
	fun keyGeneration(title : String) : String {
		val sb = StringBuilder()
		title.replace("(\\p{Ll})(\\p{Lu})","$1 $2").split(" ").forEach {
			t -> sb.append(t.substring(0,1).toUpperCase())
		}
		return sb.toString();
	}
			
	fun setProjectAsActive(projectId : Long, sprintId : Long) {
		dsl.update(PROJECT)
			.set(PROJECT.ACTIVE_SPRINT_ID, sprintId)
			.where(PROJECT.ID.eq(projectId))		
			.execute()
	}
	
	fun buildOriginSprint(userId : Long, projectId : Long, sprintLength : Int) : Long {
		
		val sprintId : Long = dsl.insertInto(SPRINT, SPRINT.START_DATE, SPRINT.END_DATE, SPRINT.PROJECT_ID)
		   .values(Timestamp(System.currentTimeMillis()), tU.inXdays(Date(), sprintLength*7), projectId)
		   .returning(SPRINT.ID).fetchOne().get(SPRINT.ID)
		
		issueRepository.createInProgress("Invite teammembers", "Please take the time to invite any other teammembers to your projects. This can be done via the project screen.", userId, sprintId, projectId, userId)
		issueRepository.createInProgress("Invite shareholders", "Please take the time to invite any shareholders to your projects. This can be done via the project screen.", userId, sprintId, projectId, userId)
				
		return sprintId		
	}
	
	override fun getCurrentVersion(projectId : Long) : ProjectVersion {
		return dsl.select(PROJECT.MAJOR_VERSION, PROJECT.MINOR_VERSION, PROJECT.PATCH_VERSION)
				.from(PROJECT)
			    .where(PROJECT.ID.eq(projectId))
			    .fetchOne()
			    .map {
				  n -> ProjectVersion(n.get(PROJECT.MAJOR_VERSION),
									n.get(PROJECT.MINOR_VERSION),
									n.get(PROJECT.PATCH_VERSION))
			   }
	}
}