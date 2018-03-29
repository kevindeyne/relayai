package com.kevindeyne.tasker.controller.timesheet

import com.kevindeyne.tasker.domain.TimesheetDay
import com.kevindeyne.tasker.domain.TimesheetEntry
import com.kevindeyne.tasker.domain.TimesheetWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*

enum class TimesheetParser() {
	
	INSTANCE;

	val tU = TimeUtils.INSTANCE

	fun getTimesheetDays(entries : List<TimesheetEntry>, startPeriod : LocalDate, endPeriod : LocalDate) : List<TimesheetWeek> {
		val r : MutableList<TimesheetWeek> = mutableListOf()
		var days : MutableList<TimesheetDay> = mutableListOf()

		val full : MutableMap<String, TimesheetDay> = mutableMapOf()

		//make list of days O(1)
		val dayRange = ChronoUnit.DAYS.between(startPeriod, endPeriod)
		for(i in 0..dayRange) {
			val date = startPeriod.plusDays(i)
			val inactive = date.monthValue != (LocalDate.now().monthValue)

			val t = TimesheetDay(date.dayOfMonth.toString(),
					date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.UK),
					tU.isToday(date),
					inactive,
					0,
					0.0,
					tU.toString(date))

			val key = tU.toString(tU.localDateToDate(date))
			full[key] = t

			days.add(t)

			if(days.size == 7){
				r.add(TimesheetWeek(days))
				days = mutableListOf()
			}
		}

		for (entry in entries) {
			val k = tU.toString(entry.startDate)
			val d = full[k]

			if (d != null){
				//TODO check if on the same day or spanning
				val min = TimeUtils.INSTANCE.countMinutesBetween(entry.startDate, entry.endDate)
				d.hours+=min/60
				d.total++

				full[k] = d
			}
		}

		if(r.size == 0 && days.size > 0) {
			r.add(TimesheetWeek(days))
		}

		return r
	}

	fun determineStartDate() : LocalDate {
		val localDate = TimeUtils.INSTANCE.today().toLocalDateTime().toLocalDate()
		return localDate.withDayOfMonth(1).with( TemporalAdjusters.previous( DayOfWeek.SUNDAY ) )
	}
	
	fun determineEndDate() : LocalDate {
		val localDate = TimeUtils.INSTANCE.today().toLocalDateTime().toLocalDate()
		return localDate.withDayOfMonth(localDate.lengthOfMonth()).with( TemporalAdjusters.next( DayOfWeek.SATURDAY ) )
	}
}