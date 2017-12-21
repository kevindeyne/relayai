package com.kevindeyne.tasker.controller.timesheet

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

enum class TimeUtils() {
	
	INSTANCE;
	
	fun localDateToDate(localDate : LocalDateTime) : Date {
		return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	fun localDateToDate(localDate : LocalDate) : Date {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	fun getDate(year : Int, month : Int, day : Int) : Date = localDateToDate(LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(day))
		
	fun areDatesOnSameDay(date1 : Date, date2 : Date) : Boolean {
		val fmt = SimpleDateFormat("yyyyMMdd");
		return fmt.format(date1).equals(fmt.format(date2));
	}
	
	fun isToday(date : LocalDate) : Boolean = areDatesOnSameDay(Date(), localDateToDate(date))
	
	fun addHours(date : Date, hours : Int) : Date {
		val ldt : LocalDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
		return Date.from(ldt.atZone(ZoneId.systemDefault()).plusHours(hours.toLong()).toInstant())
	}
	
	fun nextDay(date : Date) : Date  = addHours(date, 24)
		
	fun countMinutesBetween(date1 : Date, date2 : Date) : Int {
		val minutes : Long = ((date2.getTime() - date1.getTime()) / 1000) / 60
		return minutes.toInt()
	}
	
	fun toString(date : Date) : String  = SimpleDateFormat("yyyy-MM-dd").format(date)
	
}