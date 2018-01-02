package com.kevindeyne.tasker.repositories


import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.domain.UserPrincipal
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.UserRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
open class UserRepositoryImpl (val dsl: DSLContext, val sprintRepository : SprintRepository, val issueRepository : IssueRepository) : UserRepository {
	
	@Transactional
	override fun findByUsername(username : String) : UserPrincipal? {
		val record : Optional<UserRecord> = dsl.selectFrom(Tables.USER)
			   .where(Tables.USER.EMAIL.eq(username))
			   .fetchOptional()

		if(record.isPresent) {
			val userId : Long = getUserIdFromRecord(record.get())		
			val projectId : Long? = getProjectId(dsl, userId)
			val sprintId : Long? = sprintRepository.findCurrentSprintByProjectId(projectId)
			
			val roles = getUserRoles(userId)
			val issues = issueRepository.findAllInProgress(userId, sprintId)
			
			val up : UserPrincipal = record.get().map {
			      n -> UserPrincipal(userId,
									 n.get(Tables.USER.EMAIL),
									 n.get(Tables.USER.PASSWORD),
									 projectId,
									 sprintId,
									 roles,
									 issues.toMutableList(),
									 n.get(Tables.USER.REPORT).compareTo(1) == 0)
			   }
			
			return up;
		}
		
		return null;
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
			return record.get().map { n -> n.get(Tables.USER.EMAIL) }
		}
		
		return "System"
	}
	
	fun getUserIdFromRecord(record : UserRecord) : Long = record.get(Tables.USER.ID)
	
	fun getProjectId(dsl: DSLContext, userId : Long) : Long? {
		val projectRecord = dsl.selectFrom(Tables.PROJECT_USERS)
			   .where(Tables.PROJECT_USERS.USER_ID.eq(userId))
			   .and(Tables.PROJECT_USERS.ACTIVE.eq(true))
			   .fetchOptional()
		
		if(projectRecord.isPresent) {
			return projectRecord.get().get(Tables.PROJECT_USERS.PROJECT_ID)
		} else {
			return null
		}
	}
}