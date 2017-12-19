package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.TimesheetListing
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.TagRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.Date
import java.util.Optional
import java.util.stream.Collectors
import java.sql.Timestamp

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
		val id : Long = findOngoingTracking(issueId, userId)
		
		dsl.update(Tables.ISSUE)
			.set(Tables.TIMESHEET.END_DATE, Timestamp(System.currentTimeMillis()))
			.where(Tables.TIMESHEET.ID.eq(id))
			.execute()
	}
	
	fun findOngoingTracking(issueId: Long, userId: Long) : Long {
		return dsl.selectFrom(Tables.TIMESHEET)
			   .where(Tables.TIMESHEET.ISSUE_ID.eq(issueId)
					   .and(Tables.TIMESHEET.USER_ID.eq(userId))
					   .and(Tables.TIMESHEET.END_DATE.isNull))
			   .fetchOne().get(Tables.TIMESHEET.ID)
	}
	
	override fun getTimesheet(from : Date, until : Date, userId: Long) : List<TimesheetListing> {
		val timeFrom = Timestamp(from.getTime())
		val timeUntil = Timestamp(until.getTime())
		
		return dsl.selectFrom(Tables.TIMESHEET)
			   .where(Tables.TIMESHEET.USER_ID.eq(userId))
			   .and(Tables.TIMESHEET.START_DATE.greaterThan(timeFrom))
			   .and(Tables.TIMESHEET.END_DATE.isNull.or(Tables.TIMESHEET.END_DATE.lessThan(timeUntil)))
			   .orderBy(Tables.TIMESHEET.START_DATE.asc())
			   .fetch()
			   .parallelStream()
			   .map {
				  n -> TimesheetListing(Date(), "", "", "", 0L, "")
			   }
			   .collect(Collectors.toList())
	}
}