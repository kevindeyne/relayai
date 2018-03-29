package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.TimesheetEntry
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.TimesheetRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.*
import java.util.stream.Collectors

@Component
open class TimesheetRepositoryImpl (val dsl: DSLContext) : TimesheetRepository {
	
	@Transactional
	override fun startTrackingIssue(issueId: Long, userId: Long) {
		var avgWorkday = 8 //TODO later te configureren
		
		dsl.insertInto(Tables.TIMESHEET,
				Tables.TIMESHEET.USER_ID, Tables.TIMESHEET.ISSUE_ID, Tables.TIMESHEET.START_DATE, Tables.TIMESHEET.END_DATE, Tables.TIMESHEET.AVG_WORKDAY)
		   .values(userId, issueId, Timestamp(System.currentTimeMillis()), null, avgWorkday)
		   .execute();
	}
	
	@Transactional
	override fun stopTrackingIssue(issueId: Long, userId: Long) {
		val id : Long? = findOngoingTracking(issueId, userId)
		
		if(id != null) {
		 dsl.update(Tables.TIMESHEET)
			.set(Tables.TIMESHEET.END_DATE, Timestamp(System.currentTimeMillis()))
			.where(Tables.TIMESHEET.ID.eq(id))
			.execute()
		}
	}
	
	fun findOngoingTracking(issueId: Long, userId: Long) : Long? {
		val record : Optional<TimesheetRecord> = dsl.selectFrom(Tables.TIMESHEET)
			   .where(Tables.TIMESHEET.ISSUE_ID.eq(issueId)
					   .and(Tables.TIMESHEET.USER_ID.eq(userId))
					   .and(Tables.TIMESHEET.END_DATE.isNull))
			   .fetchOptional()
		
		if(record.isPresent){
			return record.get().get(Tables.TIMESHEET.ID)
		}
		
		return null	
	}

	override fun getTimesheet(from : Date, until : Date, userId: Long) : List<TimesheetEntry> {
		val timeFrom = Timestamp(from.time)
		val timeUntil = Timestamp(until.time)

		return dsl.selectFrom(Tables.TIMESHEET.join(Tables.ISSUE).on(Tables.ISSUE.ID.eq(Tables.TIMESHEET.ISSUE_ID)))
			   .where(Tables.TIMESHEET.USER_ID.eq(userId))
			   .and(Tables.TIMESHEET.START_DATE.greaterThan(timeFrom))
			   .and(Tables.TIMESHEET.END_DATE.isNull.or(Tables.TIMESHEET.END_DATE.lessThan(timeUntil)))
			   .orderBy(Tables.TIMESHEET.START_DATE.asc())
			   .fetch()
			   .parallelStream()
			   .map {
				  n -> TimesheetEntry(
					   n.get(Tables.TIMESHEET.START_DATE),
					   endDateOrToday(n.get(Tables.TIMESHEET.END_DATE)),
					   n.get(Tables.TIMESHEET.AVG_WORKDAY),
					   n.get(Tables.ISSUE.TITLE),
					   n.get(Tables.TIMESHEET.ISSUE_ID))
			   }
			   .collect(Collectors.toList())
	}
	
	fun endDateOrToday(n : Date?) = n ?: Date()
}