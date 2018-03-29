package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.TimesheetEntry
import java.util.*

interface TimesheetRepository {
	fun startTrackingIssue(issueId: Long, userId: Long)
	
	fun stopTrackingIssue(issueId: Long, userId: Long)
	
	fun getTimesheet(from: Date, until: Date, userId: Long) : List<TimesheetEntry>

}