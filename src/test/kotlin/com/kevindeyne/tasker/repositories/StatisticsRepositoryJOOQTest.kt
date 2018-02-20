package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.jooq.Tables.ISSUE
import com.kevindeyne.tasker.jooq.Tables.PROJECT
import com.kevindeyne.tasker.jooq.Tables.SPRINT
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockExecuteContext
import org.jooq.tools.jdbc.MockResult
import org.junit.Assert
import org.junit.Test

class StatisticsRepositoryJOOQTest : JOOQTest() {
	
	@Test
	fun testStats() {
		val dsl: DSLContext = newDSL(StatisticsProvider())

		val issueRepo = IssueRepositoryImpl(dsl)
		val sprintRepo = SprintRepositoryImpl(dsl, issueRepo)
		val projectRepo = ProjectRepositoryImpl(dsl, sprintRepo, issueRepo)
		val repo = StatisticsRepositoryImpl(dsl, sprintRepo, projectRepo)

		val stats = repo.getStats(0L, 0L)

		Assert.assertNotNull(stats)
	}

	inner class StatisticsProvider : JOOQProvider() {

		override fun execute(ctx: MockExecuteContext): Array<MockResult> {
			val dsl: DSLContext = newDSL()
			val sql = getSQLFromContext(ctx)
			
			 if (isSelectStatement(sql)) {				
	        	if(sql.contains("VERSION")) {
		        	val result = dsl.newResult(PROJECT.MAJOR_VERSION, PROJECT.MINOR_VERSION, PROJECT.PATCH_VERSION)
		            result.add(dsl.newRecord(PROJECT.MAJOR_VERSION, PROJECT.MINOR_VERSION, PROJECT.PATCH_VERSION).values(1, 2, 3))
					return arrayOf<MockResult>(MockResult(1, result))
	        	} else if(sql.contains("GROUP BY")) {
		        	val result = dsl.newResult(ISSUE.STATUS, DSL.count())
		            result.add(dsl.newRecord(ISSUE.STATUS, DSL.count()).values("NEW", 5))
					return arrayOf<MockResult>(MockResult(1, result))
	        	} else if(sql.contains("END_DATE")) {
	        		val result = dsl.newResult(SPRINT.START_DATE, SPRINT.END_DATE)
		            result.add(dsl.newRecord(SPRINT.START_DATE, SPRINT.END_DATE).values(TimeUtils.INSTANCE.today(), TimeUtils.INSTANCE.inXdays(TimeUtils.INSTANCE.today(), 14)))
					return arrayOf<MockResult>(MockResult(1, result))
	        	} else if(sql.startsWith("SELECT COUNT(*)")) {
	        		val result = dsl.newResult(DSL.count())
		            result.add(dsl.newRecord(DSL.count()).values(20))
					return arrayOf<MockResult>(MockResult(1, result))
	        	}
	        }
			
			return arrayOf<MockResult>()
		}
	}

}