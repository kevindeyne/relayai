package com.kevindeyne.tasker.domain

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import java.sql.Date
import java.sql.Timestamp

data class SprintDates constructor(
		val sprintNr: Int = 1,
		val startDate: Timestamp = Timestamp(0),
		val endDate: Timestamp = Timestamp(0)
) {
	fun getTotalDays(): Double {
		return TimeUtils.INSTANCE.countDaysBetween(startDate, endDate).toDouble()
	}

	fun daysUntil(): Double {
		return TimeUtils.INSTANCE.countDaysBetween(TimeUtils.INSTANCE.today(), endDate).toDouble()
	}
}