package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.jooq.Tables
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.sql.Timestamp

@Component
open class SprintRepositoryImpl (val dsl: DSLContext) : SprintRepository {
		
	override fun findCurrentSprintByProjectId(projectId : Long?) : Long? {
		if(projectId != null) {
			val currentTime = Timestamp(System.currentTimeMillis());
			val sprintRecord =  dsl.selectFrom(Tables.SPRINT)
					.where(Tables.SPRINT.PROJECT_ID.eq(projectId))
					.and(Tables.SPRINT.START_DATE.lessOrEqual(currentTime))
					.and(Tables.SPRINT.END_DATE.greaterOrEqual(currentTime))
					.fetchOptional()
			
			if(sprintRecord.isPresent) {
				return sprintRecord.get().get(Tables.SPRINT.ID)
			} else {
				return null
			}
		}
		return null;
	}
	
}