package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.ProjectListing
import com.kevindeyne.tasker.jooq.Tables
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors
import com.kevindeyne.tasker.service.SecurityHolder

@Component
open class ProjectRepositoryImpl (val dsl: DSLContext, val sprintRepository : SprintRepository) : ProjectRepository {
	
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
	
	override fun findActiveProject(userId : Long) : ProjectListing {
		return dsl.select()
				.from(Tables.PROJECT)
				.join(Tables.PROJECT_USERS).on(Tables.PROJECT_USERS.PROJECT_ID.eq(Tables.PROJECT.ID))
			    .where(Tables.PROJECT_USERS.USER_ID.eq(userId))
			    .and(Tables.PROJECT_USERS.ACTIVE.eq(true))
			    .fetchOne()
			    .map {
				  n -> ProjectListing(n.get(Tables.PROJECT.ID),
									n.get(Tables.PROJECT.TITLE),
									n.get(Tables.PROJECT.KEY))
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
}