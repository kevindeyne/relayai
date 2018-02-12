package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.domain.StatisticsListing
import com.kevindeyne.tasker.jooq.Tables
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Component
import java.sql.Timestamp

@Component
open class StatisticsRepositoryImpl (val dsl: DSLContext, val sprintRepo: SprintRepository, val projectRepo: ProjectRepository) : StatisticsRepository {
	
	override fun getStats(sprintId : Long, projectId : Long) : StatisticsListing {
		val stats = StatisticsListing()
		
		val statusCounts = getStatusCounts(sprintId)
		
		statusCounts.keys.forEach{
			k ->
				val count = statusCounts.get(k)
				stats.issuePlannedTotal += count ?: 0
			
				if(Progress.DONE.equals(Progress.valueOf(k))){
					stats.issuePlannedDone = count ?: 0
				} else if(Progress.WAITING_FOR_FEEDBACK.equals(Progress.valueOf(k))){
					stats.issuesWaitingForFeedback = count ?: 0
					stats.issuePlannedActive += count ?: 0
				} else {
					stats.issuePlannedActive += count ?: 0
				}
		}
		
		stats.issueCompletionRate = (stats.issuePlannedDone.toDouble().div(stats.issuePlannedTotal.toDouble()) * 100).toInt()
		stats.waitingForFeedbackRate = (stats.issuesWaitingForFeedback.toDouble().div(stats.issuePlannedActive.toDouble()) * 100).toInt()
		
		val sprintDates = sprintRepo.findSprintEndDate(sprintId)
		val totalDays = sprintDates.getTotalDays()
		val daysUntil = sprintDates.daysUntil()
		val progressDays : Double = totalDays - daysUntil
		
		stats.daysUntilRelease = daysUntil.toInt()	
		stats.sprintCompletionRate = (progressDays.div(totalDays) * 100).toInt()

		val backlogIssues = backlogSinceSprintStart(sprintDates.startDate, sprintId)
		val backlogPercentage = (backlogIssues.toDouble().div(totalDays) * 100).toInt()
		
		stats.issuesAddedSinceSprintCreation = backlogIssues
		stats.backlogEvolutionRate = if(backlogIssues > 0) { "+$backlogPercentage" } else { "$backlogPercentage" }
		
		return stats
	}
	
	override fun getStatusCounts(sprintId : Long) : Map<String, Int> {
		return dsl.select(Tables.ISSUE.STATUS, DSL.count())
			   .from(Tables.SPRINT.join(Tables.ISSUE).on(Tables.ISSUE.SPRINT_ID.eq(Tables.SPRINT.ID)))			   
			   .where(Tables.SPRINT.ID.eq(sprintId)
					   .and(Tables.ISSUE.STATUS.notIn(Progress.NEW.name)))
				.groupBy(Tables.ISSUE.STATUS)
			   .fetch()
			   .intoMap(Tables.ISSUE.STATUS, DSL.count())
	}
	
	//TODO kan ook negatief zijn
	fun backlogSinceSprintStart(startDate : Timestamp, projectId : Long) : Int {
		return dsl.fetchCount(
		dsl.selectFrom(Tables.ISSUE)
	    .where(Tables.ISSUE.PROJECT_ID.eq(projectId)
	       .and(Tables.ISSUE.CREATE_DATE.gt(startDate))
	       .and(
			(Tables.ISSUE.STATUS.eq(Progress.NEW.name))
			.or(Tables.ISSUE.STATUS.eq(Progress.BACKLOG.name))
		   )))
	}
}