package com.kevindeyne.tasker.controller.timesheet

import com.kevindeyne.tasker.domain.TimesheetDay
import com.kevindeyne.tasker.domain.TimesheetEntry
import com.kevindeyne.tasker.domain.TimesheetWeek
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.*

enum class TimesheetParser() {
	
	INSTANCE;
	
	val tU = TimeUtils.INSTANCE
	val format = SimpleDateFormat("yyyyMMdd")
	
	fun getTimesheetDays(entries : List<TimesheetEntry>, startPeriod : LocalDate, endPeriod : LocalDate) : List<TimesheetWeek> {
		val result : MutableList<TimesheetWeek> = mutableListOf()
		var days : MutableList<TimesheetDay> = mutableListOf()

		val full : MutableMap<Date, TimesheetDay> = mutableMapOf()

		//make list of days O(1)
		val dayRange = ChronoUnit.DAYS.between(startPeriod, endPeriod)
		for(i in 0..dayRange) {
			val date = startPeriod.plusDays(i)
			val inactive = date.monthValue != (LocalDate.now().monthValue)

			val t = TimesheetDay(date.dayOfMonth.toString(),
					date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.UK),
					tU.isToday(date),
					inactive,
					0)

			full.put(tU.localDateToDate(date), t)
			days.add(t)

			if(days.size == 7){
				result.add(TimesheetWeek(days))
				days = mutableListOf()
			}
		}

		for (entry in entries) {
			val d = full[entry.startDate]
			if (d != null){
				d.total++
			}
		}

		return result
	}

	fun determineStartDate(date : Date = Date()) : LocalDate {
		val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		return localDate.withDayOfMonth(1).with( TemporalAdjusters.previous( DayOfWeek.SUNDAY ) )
	}
	
	fun determineEndDate(date : Date = Date()) : LocalDate {
		val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate.withDayOfMonth(localDate.lengthOfMonth()).with( TemporalAdjusters.next( DayOfWeek.SATURDAY ) )
	}
}