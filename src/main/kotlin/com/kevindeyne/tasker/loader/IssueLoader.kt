package com.kevindeyne.tasker.loader

import com.github.javafaker.Faker
import com.kevindeyne.tasker.domain.Impact
import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.domain.SearchResultType
import com.kevindeyne.tasker.domain.Urgency
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.repositories.IssueRepositoryImpl
import com.kevindeyne.tasker.repositories.TagcloudRepository
import com.kevindeyne.tasker.service.KeywordGeneration
import org.jooq.DSLContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.Ordered
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDate
import java.util.Random
import java.util.stream.Collectors
import com.kevindeyne.tasker.config.RealdataFaker

@Component
class IssueLoader(
		val dsl: DSLContext,
		val passwordEncoder : PasswordEncoder,
		val tagcloudRepository : TagcloudRepository
) : ApplicationListener<ContextRefreshedEvent>, Ordered {
	
	val truncateAll : Boolean = true
	val generateNew : Boolean = true
	val maxUserIssuesInSprint : Int = 40
	val daysPerSprint : Int = 14
	val totalAmountOfSprints : Int = 10
	val currentUserRole = Role.DEVELOPER
	
	val maxInProgress = 4
	var inProgressIssues = 0
	
	override fun getOrder() : Int {
		return 1
	}
	
	@Transactional
	override fun onApplicationEvent(event: ContextRefreshedEvent) {
		val faker : Faker = Faker()
		
		if(truncateAll) {	
			truncateAll()
			if(!generateNew) {
				generateUsers(faker)
			}
		}
		
		if(generateNew) {    
        	println("Generating issues ...")

        	val userId = generateUsers(faker)
			
        	val projectId = generateProjects(faker)
			val projectId2 = generateProjects(faker)
			assert(projectId != projectId2)
			
        	attachProjectAndUser(projectId, userId, true)
			attachProjectAndUser(projectId2, userId, false)
        	
			val sprintId = generateSprints(projectId)
			val sprintId2 = generateSprints(projectId2)
			
			assert(sprintId != sprintId2)
			
			setActiveProject(projectId, sprintId)
			setActiveProject(projectId2, sprintId2)

        	var currentUserIssuesInSprint = 0
			
			for(i in 0..250 + maxUserIssuesInSprint){
				var assignedTo = getRandomUser()
				if(maxUserIssuesInSprint > currentUserIssuesInSprint++) {
					assignedTo = userId
				}

				insertIntoIssue(faker, userId, projectId, sprintId, assignedTo)
			}
			
			currentUserIssuesInSprint = 0
			for(i in 0..250 + maxUserIssuesInSprint){
				var assignedTo = getRandomUser()
				if(maxUserIssuesInSprint > currentUserIssuesInSprint++) {
					assignedTo = userId
				}

				insertIntoIssue(faker, userId, projectId2, sprintId2, assignedTo)
			}

			println("Done!")
    	}
	}
	
	@Transactional
	fun truncateAll() {		
		dsl.delete(Tables.VERSION_ISSUE).execute()		
		dsl.delete(Tables.VERSIONS).execute()
		dsl.delete(Tables.BRANCH).execute()		
		dsl.delete(Tables.COMMENTS).execute()
		dsl.delete(Tables.PROJECT_USERS).execute()
		dsl.delete(Tables.TAGCLOUD).execute()
		dsl.delete(Tables.KNOWLEDGE).execute()
		dsl.delete(Tables.SEARCH).execute()
		dsl.delete(Tables.TAG).execute()
		dsl.delete(Tables.TIMESHEET).execute()
		dsl.delete(Tables.USER_ROLE).execute()
		dsl.delete(Tables.USER).execute()
		dsl.delete(Tables.ISSUE).execute()
		dsl.delete(Tables.SPRINT).execute()
		dsl.delete(Tables.PROJECT).execute()
	}
	
	fun generateTitle() : String = RealdataFaker.INSTANCE.list.get(Random().nextInt(RealdataFaker.INSTANCE.list.size)) 
		
	fun getRandomUser() : Long {
		val r = Random()
		return dsl.selectFrom(Tables.USER).fetch().parallelStream().map{u -> u.get(Tables.USER.ID) }.collect(Collectors.toList()).get(r.nextInt(100))
	}	
							
	fun setActiveProject(projectId : Long, sprintId : Long) {
		dsl.update(Tables.PROJECT)
			.set(Tables.PROJECT.ACTIVE_SPRINT_ID, sprintId)
			.where(Tables.PROJECT.ID.eq(projectId))
			.execute()
	}
	
	
	fun generateUsers(faker : Faker) : Long {
		val userId = insertIntoUser(faker.name().fullName(), "admin", passwordEncoder.encode("admin"))
		
		for(i in 0..500){
			val email = faker.internet().emailAddress()
			if(!"admin".equals(email)){
				insertIntoUser(faker.name().fullName(), email,  faker.internet().password())	
			}			
		}
		return userId;
	}
	
	
	fun generateProjects(faker : Faker) : Long {
		var projectId = -1L;
		for(i in 0..500){
			projectId = dsl.insertInto(Tables.PROJECT, Tables.PROJECT.TITLE, Tables.PROJECT.KEY)
			   .values("${faker.space().nasaSpaceCraft()} ${faker.space().nebula()}", faker.space().agencyAbbreviation())
			   .returning(Tables.PROJECT.ID).fetchOne().get(Tables.PROJECT.ID);
		}

		return projectId
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
	
	
	fun attachProjectAndUser(projectId : Long, userId : Long, active : Boolean) =
			dsl.insertInto(Tables.PROJECT_USERS,
			  Tables.PROJECT_USERS.PROJECT_ID, Tables.PROJECT_USERS.USER_ID, Tables.PROJECT_USERS.ACTIVE)
		   .values(projectId, userId, active)
		   .execute()
	
	
	fun insertSprint(startTime : LocalDate, endTime : LocalDate, projectId : Long) : Long =
		dsl.insertInto(Tables.SPRINT, Tables.SPRINT.START_DATE, Tables.SPRINT.END_DATE, Tables.SPRINT.PROJECT_ID)
		   .values(Timestamp.valueOf(startTime.atStartOfDay()), Timestamp.valueOf(endTime.atStartOfDay()), projectId)
		   .returning(Tables.SPRINT.ID).fetchOne().get(Tables.SPRINT.ID)
	
	fun insertComment(userId : Long, issueId : Long, text : String) : Long = dsl.insertInto(Tables.COMMENTS,
			Tables.COMMENTS.USER_ID, Tables.COMMENTS.ISSUE_ID, Tables.COMMENTS.MESSAGE, Tables.COMMENTS.POST_DATE)
		   .values(userId, issueId, text, Timestamp(System.currentTimeMillis()))
		   .returning(Tables.COMMENTS.ID).fetchOne().get(Tables.COMMENTS.ID)
	
	fun insertIntoUser(fullName : String, username : String, password : String) : Long {
		val userId = dsl.insertInto(Tables.USER,
			 Tables.USER.EMAIL, Tables.USER.PASSWORD, Tables.USER.USERNAME, Tables.USER.CREATE_DATE, Tables.USER.CREATE_USER, Tables.USER.UPDATE_DATE, Tables.USER.UPDATE_USER)
		   .values(username, password, fullName, getRandomTimestamp(), randomUser(), getRandomTimestamp(), randomUser())
		   .returning(Tables.USER.ID).fetchOne().get(Tables.USER.ID)
		
		dsl.insertInto(Tables.USER_ROLE,
			 Tables.USER_ROLE.USER_ID, Tables.USER_ROLE.ROLE)
		   .values(userId, currentUserRole.name)
		   .execute()
		
		return userId
	}
	
	fun insertIntoIssue(faker : Faker, userId : Long, projectId : Long, sprintId : Long, assignedTo : Long) {
		val title = generateTitle()
		val sentenceList : MutableList<String> = ArrayList<String>()
		for (i in 0..5) {
			sentenceList.add(faker.harryPotter().quote())
		}
		val sentences = mergeSentences(sentenceList)
		
		val workload = randomWorkload()
		val status = randomStatus(workload)
		val impact = randomImpact(workload)
		val urgency = randomUrgency(workload)
		val importance = IssueRepositoryImpl(dsl).determineImportance(status, userId, workload, impact, urgency)
		
		val issueId = dsl.insertInto(Tables.ISSUE, Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.WORKLOAD, Tables.ISSUE.STATUS,
				Tables.ISSUE.PROJECT_ID, Tables.ISSUE.SPRINT_ID, Tables.ISSUE.ASSIGNED, Tables.ISSUE.CREATE_DATE, Tables.ISSUE.CREATE_USER, Tables.ISSUE.UPDATE_DATE,
				Tables.ISSUE.UPDATE_USER, Tables.ISSUE.URGENCY, Tables.ISSUE.IMPACT, Tables.ISSUE.IMPORTANCE, Tables.ISSUE.OVERLOAD)
		   .values(title, sentences, workload, status.name, projectId, sprintId, assignedTo, getRandomTimestamp(), randomUser(), getRandomTimestamp(), randomUser(),
				   urgency.name, impact.name, importance, randomBoolean())
		   .returning(Tables.ISSUE.ID).fetchOne().get(Tables.ISSUE.ID);
		
		for(i in 1..Random().nextInt(10)) {
			insertComment(assignedTo, issueId, faker.gameOfThrones().quote())	
		}
		
		var joinedText = "$title $sentences"
		joinedText = joinedText.toLowerCase()
		
		KeywordGeneration.generateKeywords(joinedText).forEach{k -> tagcloudRepository.addToIssueIfNotExists(k, issueId)}
		
		dsl.insertInto(Tables.SEARCH, Tables.SEARCH.PROJECT_ID, Tables.SEARCH.TYPE, Tables.SEARCH.SRCVAL, Tables.SEARCH.NAME, Tables.SEARCH.LINKED_ID)
		.values(projectId, SearchResultType.ISSUE.name, joinedText, "$title", issueId)
		.returning(Tables.SEARCH.ID).fetchOne().get(Tables.SEARCH.ID);
	}
	
	fun randomBoolean() : Byte = if (Random().nextInt(10) > 7) { 0.toByte() } else { 1.toByte() }
	fun randomWorkload() : Int = Random().nextInt(9) - 1
	fun randomUrgency(workload : Int) : Urgency = if(workload != -1) Urgency.values()[Random().nextInt(Urgency.values().size)] else Urgency.NORMAL
	fun randomImpact(workload : Int) : Impact = if(workload != -1) Impact.values()[Random().nextInt(Impact.values().size)] else Impact.NORMAL
	fun randomStatus(workload : Int) : Progress {		
		if(workload != -1) {
			val returnValue = Progress.values()[Random().nextInt(Progress.values().size)]			
			if(Progress.IN_PROGRESS.equals(returnValue)){
				inProgressIssues++
				if(inProgressIssues > maxInProgress){
					return Progress.IN_SPRINT
				}
			}			
			return returnValue
		}  else {
			return Progress.NEW
		}	
	}

	fun randomUser() : String = Random().nextInt(450).toString()
	
	fun mergeSentences(sentences : List<String>) : String {
		val sb : StringBuffer = StringBuffer()
		sentences.forEach{s -> sb.append(s + " ")}
		return sb.toString()
	}

	fun getRandomTimestamp() : Timestamp = Timestamp(System.currentTimeMillis())
	
}