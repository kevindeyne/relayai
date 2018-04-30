package com.kevindeyne.tasker.repositories


import com.kevindeyne.tasker.domain.ProjectListing
import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.domain.TeammemberListing
import com.kevindeyne.tasker.domain.UserPrincipal
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.UserRecord
import org.jooq.DSLContext
import org.jooq.TableField
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.*
import java.util.stream.Collectors

@Repository
open class UserRepositoryImpl (val dsl: DSLContext, val sprintRepository : SprintRepository, val projectRepository : ProjectRepository) : UserRepository {
	
	@Transactional
	override fun findByUsername(username : String) : UserPrincipal? {
		val record : Optional<UserRecord> = dsl.selectFrom(Tables.USER)
			   .where(Tables.USER.EMAIL.eq(username))
			   .fetchOptional()

		if(record.isPresent) {
			val userId : Long = getUserIdFromRecord(record.get())
			val project : ProjectListing? = projectRepository.findActiveProject(userId)
			var projectId = -1L
			var sprintId : Long = -1L
			
			if(project != null){
				projectId = project.id				
				sprintId = sprintRepository.findCurrentSprintByProjectId(projectId)				
			}

			val roles = getUserRoles(userId)

			val up : UserPrincipal = record.get().map {
			      n -> UserPrincipal(userId,
									 n.get(Tables.USER.EMAIL),
									 n.get(Tables.USER.PASSWORD),
									 projectId,
									 sprintId,
									 roles,
									 mutableListOf(),
									 n.get(Tables.USER.REPORT).compareTo(1) == 0)
			   }
			
			return up;
		}
		
		return null;
	}

	@Transactional
	override fun create(username : String, email : String, password : String) : Long {
		val now = Timestamp(System.currentTimeMillis())
		val sys = "[System]"

		val userId = dsl.insertInto(Tables.USER,
				Tables.USER.EMAIL, Tables.USER.PASSWORD, Tables.USER.USERNAME, Tables.USER.CREATE_DATE, Tables.USER.CREATE_USER, Tables.USER.UPDATE_DATE, Tables.USER.UPDATE_USER)
				.values(email, password, username, now, sys, now, sys)
				.returning(Tables.USER.ID).fetchOne().get(Tables.USER.ID)

		dsl.insertInto(Tables.USER_ROLE,
				Tables.USER_ROLE.USER_ID, Tables.USER_ROLE.ROLE)
				.values(userId, Role.TEAM_LEADER.name)
				.execute()

		return userId
	}
	
	fun getUserRoles(userId : Long) : List<Role> {
		return dsl.selectFrom(Tables.USER_ROLE)
			   .where(Tables.USER_ROLE.USER_ID.eq(userId))
			   .fetch().map{
					n -> Role.valueOf(n.get(Tables.USER_ROLE.ROLE)) 
				}
	}
	
	override fun findUsernameById(id : String) : String {
		val record : Optional<UserRecord> = dsl.selectFrom(Tables.USER)
			   .where(Tables.USER.ID.eq(id.toLong()))
			   .fetchOptional()
		
		if(record.isPresent) {
			return record.get().map { n -> n.get(Tables.USER.USERNAME) }
		}
		
		return "[System]"
	}

	override fun findElementById(element : TableField<UserRecord, String>, id : Long) : String {
		val record : Optional<UserRecord> = dsl.selectFrom(Tables.USER)
				.where(Tables.USER.ID.eq(id))
				.fetchOptional()

		if(record.isPresent) {
			return record.get().map { n -> n.get(element) }
		}

		throw RuntimeException("Could not find element for userId [$id]")
	}
	
	fun getUserIdFromRecord(record : UserRecord) : Long = record.get(Tables.USER.ID)
		
	override fun findTeammembersByProject(projectId : Long) : List<TeammemberListing> {
		return dsl.select()
				  .from(Tables.USER
							.join(Tables.PROJECT_USERS)
							.on(Tables.PROJECT_USERS.USER_ID.eq(Tables.USER.ID))
							.join(Tables.USER_ROLE)
							.on(Tables.USER_ROLE.USER_ID.eq(Tables.USER.ID))
						)
			   .where(Tables.PROJECT_USERS.PROJECT_ID.eq(projectId))
			   .orderBy(Tables.USER_ROLE.ROLE)
			   .fetch()
			   .parallelStream()
			   .map {
				  n -> TeammemberListing(n.get(Tables.USER.ID),
									n.get(Tables.USER.USERNAME),
									Role.valueOf(n.get(Tables.USER_ROLE.ROLE)).text)
			   }
			   .collect(Collectors.toList())
	}
	
	override fun findInvitesByProject(projectId : Long) : List<TeammemberListing> {
		return dsl.select()
				  .from(Tables.INVITATION)
			   .where(Tables.INVITATION.PROJECT_ID.eq(projectId))
			   .orderBy(Tables.INVITATION.ROLE)
			   .fetch()
			   .parallelStream()
			   .map {
				  n -> TeammemberListing(n.get(Tables.INVITATION.ID),
									n.get(Tables.INVITATION.EMAIL),
									Role.valueOf(n.get(Tables.INVITATION.ROLE)).text)
			   }
			   .collect(Collectors.toList())
	}

	override fun addUserToProject(userId : Long, projectID : Long) {
		val projectCount = dsl.fetchCount(dsl.selectFrom(Tables.PROJECT_USERS).where(Tables.PROJECT_USERS.USER_ID.eq(userId)))
		dsl.insertInto(Tables.PROJECT_USERS,
				Tables.PROJECT_USERS.PROJECT_ID, Tables.PROJECT_USERS.USER_ID, Tables.PROJECT_USERS.ACTIVE)
				.values(projectID, userId, 0 == projectCount)
				.execute()
	}
}