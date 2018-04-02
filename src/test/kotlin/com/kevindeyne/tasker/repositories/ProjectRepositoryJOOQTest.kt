package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.jooq.Tables.PROJECT
import org.jooq.DSLContext
import org.jooq.tools.jdbc.MockExecuteContext
import org.jooq.tools.jdbc.MockResult
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class ProjectRepositoryJOOQTest : JOOQTest() {

	@Test
	fun testFindProject() {
		val dsl: DSLContext = newDSL(ProjectProvider())

		val issueRepo = IssueRepositoryImpl(dsl)
		val sprintRepo = SprintRepositoryImpl(dsl)

		val repo = ProjectRepositoryImpl(dsl, sprintRepo, issueRepo)
		val project = repo.findProject(1L)

		Assert.assertNotNull(project)
		Assert.assertNotNull(project.title)
		Assert.assertNotNull(project.key)
		Assert.assertNotNull(project.fullTitle())
	}

	@Test @Ignore
	fun testGetCurrentVersion() {
		val dsl: DSLContext = newDSL(ProjectProvider())

		val issueRepo = IssueRepositoryImpl(dsl)
		val sprintRepo = SprintRepositoryImpl(dsl)

		val repo = ProjectRepositoryImpl(dsl, sprintRepo, issueRepo)
		val version = repo.getCurrentVersion(1L)

		Assert.assertNotNull(version);
		Assert.assertNotNull(version.majorVersion)
		Assert.assertNotNull(version.minorVersion)
		Assert.assertNotNull(version.patchVersion)
	}

	inner class ProjectProvider : JOOQProvider() {

		override fun execute(ctx: MockExecuteContext): Array<MockResult> {
			val dsl: DSLContext = newDSL()
			val sql = getSQLFromContext(ctx)

			if (isSelectStatement(sql)) {
				if (sql.contains("VERSION")) {
					/*val result = dsl.newResult(PROJECT.MAJOR_VERSION, PROJECT.MINOR_VERSION, PROJECT.PATCH_VERSION)
					result.add(dsl.newRecord(PROJECT.MAJOR_VERSION, PROJECT.MINOR_VERSION, PROJECT.PATCH_VERSION).values(1, 2, 3))
					return arrayOf<MockResult>(MockResult(1, result))*/
				} else {
					val result = dsl.newResult(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY)
					result.add(dsl.newRecord(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY).values(1L, "Orwell", "Key"))
					return arrayOf<MockResult>(MockResult(1, result))
				}
			}

			return arrayOf<MockResult>()
		}
	}

}