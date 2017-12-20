package com.kevindeyne.tasker.controller.timesheet

import com.kevindeyne.tasker.domain.TimesheetEntry
import com.kevindeyne.tasker.domain.TimesheetListing
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

enum class TimeUtils() {
	
	INSTANCE;
	
	fun localDateToDate(localDate : LocalDateTime) : Date {
		return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	fun areDatesOnSameDay(date1 : Date, date2 : Date) : Boolean {
		val fmt = SimpleDateFormat("yyyyMMdd");
		return fmt.format(date1).equals(fmt.format(date2));
	}
	
	fun addHours(date : Date, hours : Int) : Date {
		val ldt : LocalDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
		return Date.from(ldt.atZone(ZoneId.systemDefault()).plusHours(hours.toLong()).toInstant())
	}
	
	fun nextDay(date : Date) : Date  = addHours(date, 24)
	
	fun countHoursBetween(date1 : Date, date2 : Date) : Int {
		val hours : Long = ((date2.getTime() - date1.getTime()) / 1000) / 3600
		return hours.toInt()
	}
	
	fun toString(date : Date) : String  = "Donderdag" //TODO
}