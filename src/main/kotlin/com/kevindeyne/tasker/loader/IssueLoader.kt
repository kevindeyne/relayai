package com.kevindeyne.tasker.loader

import com.github.javafaker.Faker
import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.domain.SearchResultType
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.repositories.TagcloudRepository
import com.kevindeyne.tasker.service.KeywordGeneration
import org.jooq.DSLContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.Ordered
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDate

@Component
class IssueLoader(
		val dsl: DSLContext,
		val passwordEncoder : PasswordEncoder,
		val tagcloudRepository : TagcloudRepository
) : ApplicationListener<ContextRefreshedEvent>, Ordered {
	
	val generateNew : Boolean = true
	val maxUserIssuesInSprint : Int = 40
	val daysPerSprint : Int = 14
	val totalAmountOfSprints : Int = 500
	val currentUserRole = Role.DEVELOPER

	override fun getOrder() : Int {
		return 1
	}
	
	override fun onApplicationEvent(event: ContextRefreshedEvent?) {
		if(generateNew) {
    		truncateAll()

    		val faker : Faker = Faker()
        	println("Generating issues ...")

        	val userId = generateUsers(faker)
        	val projectId = generateProjects(faker)
        	attachProjectAndUser(projectId, userId)
        	val sprintId = generateSprints(projectId)

        	var currentUserIssuesInSprint = 0
			
			for(i in 0..500 + maxUserIssuesInSprint){
				var assignedTo = 400L
				if(maxUserIssuesInSprint > currentUserIssuesInSprint++) {
					assignedTo = userId
				}

				insertIntoIssue(faker, projectId, sprintId, assignedTo)
			}

			println("Done!")
    	}
	}	
	
	fun truncateAll() {
		dsl.truncate(Tables.ISSUE).execute();
		dsl.truncate(Tables.KNOWLEDGE).execute();
		dsl.truncate(Tables.SEARCH).execute();
		dsl.truncate(Tables.SPRINT).execute();
		dsl.truncate(Tables.TAG).execute();
		dsl.truncate(Tables.TAGCLOUD).execute();
		dsl.truncate(Tables.TIMESHEET).execute();
		dsl.truncate(Tables.PROJECT).execute();				
		dsl.truncate(Tables.PROJECT_USERS).execute();
		dsl.truncate(Tables.USER).execute();
		dsl.truncate(Tables.USER_ROLE).execute();
	}
	
	fun generateUsers(faker : Faker) : Long{
		val userId = insertIntoUser("admin", passwordEncoder.encode("admin"))
		for(i in 0..500){
			insertIntoUser(faker.internet().emailAddress(),  faker.internet().password())
		}
		return userId;
	}
	
	fun generateProjects(faker : Faker) : Long{
		for(i in 0..500){
			dsl.insertInto(Tables.PROJECT, Tables.PROJECT.TITLE, Tables.PROJECT.KEY)
			   .values(faker.space().nasaSpaceCraft(), faker.space().agencyAbbreviation())
			   .execute()
		}

		return dsl.selectFrom(Tables.PROJECT).fetchAny().get(Tables.PROJECT.ID)
	}
	
	fun generateSprints(projectId : Long) : Long{
		val currentSprintId = insertSprint(LocalDate.now().minusDays(1), LocalDate.now().plusDays(14), projectId);
		val daysInThePast = totalAmountOfSprints*daysPerSprint + totalAmountOfSprints
		var startTime = LocalDate.now().minusDays(daysInThePast.toLong())
		
		for (i in 1..totalAmountOfSprints) {
			insertSprint(startTime, startTime.plusDays(14), projectId);
			startTime = startTime.plusDays(15);
		}

		return currentSprintId;
	}
	
	fun attachProjectAndUser(projectId : Long, userId : Long) =
			dsl.insertInto(Tables.PROJECT_USERS,
			  Tables.PROJECT_USERS.PROJECT_ID, Tables.PROJECT_USERS.USER_ID, Tables.PROJECT_USERS.ACTIVE)
		   .values(projectId, userId, true)
		   .execute()
	
	
	fun insertSprint(startTime : LocalDate, endTime : LocalDate, projectId : Long) : Long =
		dsl.insertInto(Tables.SPRINT, Tables.SPRINT.START_DATE, Tables.SPRINT.END_DATE, Tables.SPRINT.PROJECT_ID)
		   .values(Timestamp.valueOf(startTime.atStartOfDay()), Timestamp.valueOf(endTime.atStartOfDay()), projectId)
		   .returning(Tables.SPRINT.ID).fetchOne().get(Tables.SPRINT.ID)
	
	fun insertIntoUser(username : String, password : String) : Long {
		val userId = dsl.insertInto(Tables.USER,
			 Tables.USER.EMAIL, Tables.USER.PASSWORD, Tables.USER.CREATE_DATE, Tables.USER.CREATE_USER, Tables.USER.UPDATE_DATE, Tables.USER.UPDATE_USER)
		   .values(username, password, getRandomTimestamp(), "1", getRandomTimestamp(), "1")
		   .returning(Tables.USER.ID).fetchOne().get(Tables.USER.ID)
		
		dsl.insertInto(Tables.USER_ROLE,
			 Tables.USER_ROLE.USER_ID, Tables.USER_ROLE.ROLE)
		   .values(userId, currentUserRole.name)
		   .execute()
		
		return userId
	}
	
	fun insertIntoIssue(faker : Faker, projectId : Long, sprintId : Long, assignedTo : Long) {
		val title = faker.book().title()
		val sentenceList : MutableList<String> = ArrayList<String>()
		for (i in 0..5) {
			sentenceList.add(faker.harryPotter().quote())
		}
		val sentences = mergeSentences(sentenceList)
	
		val issueId = dsl.insertInto(Tables.ISSUE, Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION,Tables.ISSUE.PROJECT_ID, Tables.ISSUE.SPRINT_ID, Tables.ISSUE.ASSIGNED, Tables.ISSUE.CREATE_DATE, Tables.ISSUE.CREATE_USER, Tables.ISSUE.UPDATE_DATE, Tables.ISSUE.UPDATE_USER)
		   .values(title, sentences, projectId, sprintId, assignedTo, getRandomTimestamp(), "1", getRandomTimestamp(), "1")
		   .returning(Tables.ISSUE.ID).fetchOne().get(Tables.ISSUE.ID);
		
		var joinedText = "$title $sentences"
		joinedText = joinedText.toLowerCase()
		
		KeywordGeneration.generateKeywords(joinedText).forEach{k -> tagcloudRepository.addToIssueIfNotExists(k, issueId)}
		
		dsl.insertInto(Tables.SEARCH, Tables.SEARCH.PROJECT_ID, Tables.SEARCH.TYPE, Tables.SEARCH.SRCVAL, Tables.SEARCH.NAME, Tables.SEARCH.LINKED_ID)
		.values(projectId, SearchResultType.ISSUE.name, joinedText, "$title", issueId)
		.returning(Tables.SEARCH.ID).fetchOne().get(Tables.SEARCH.ID);
	}
	
	fun mergeSentences(sentences : List<String>) : String {
		val sb : StringBuffer = StringBuffer()
		sentences.forEach{s -> sb.append(s + " ")}
		return sb.toString()
	}

	fun getRandomTimestamp() : Timestamp = Timestamp(System.currentTimeMillis())
	
}