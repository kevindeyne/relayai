package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.TimesheetListing
import java.util.Date

interface TimesheetRepository {
	fun startTrackingIssue(issueId: Long, userId: Long)
	
	fun stopTrackingIssue(issueId: Long, userId: Long)
	
	fun getTimesheet(from : Date, until : Date, userId: Long) : List<TimesheetListing>
}