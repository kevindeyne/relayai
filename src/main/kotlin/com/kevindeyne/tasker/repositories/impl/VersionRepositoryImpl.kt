package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.jooq.Tables
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
open class VersionRepositoryImpl (val dsl: DSLContext) : VersionRepository {

	override fun findTop10MostRecentVersionsInProject(projectId : Long): List<String> {
		return dsl
		 .select(Tables.VERSIONS.VERSION)
		 .from(Tables.VERSIONS)
		 .where(Tables.VERSIONS.PROJECT_ID.eq(projectId))
		 .orderBy(Tables.VERSIONS.VERSION)
    	 .limit(10)
		 .fetch().map{
			n -> n.toString()
		 }
	}
}