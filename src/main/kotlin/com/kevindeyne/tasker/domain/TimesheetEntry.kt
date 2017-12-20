package com.kevindeyne.tasker.domain

import java.util.Date

data class TimesheetEntry constructor(
		val startDate : Date,
		val endDate : Date,
		val avgWorkday: Int = 8,
		val issueName: String = "",
		val issueId: Long = 0L
)  {
	
	fun cloneWithEnddate(startDate : Date, endDate : Date) : TimesheetEntry {
		return TimesheetEntry(startDate,
		endDate,
		this.avgWorkday,
		this.issueName,
		this.issueId)
	}
		
}