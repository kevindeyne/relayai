package com.kevindeyne.tasker.controller.timesheet

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

enum class TimeUtils {
	
	INSTANCE;

	fun localDateToDate(localDate : LocalDateTime) : Date {
		return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	fun localDateToDate(localDate : LocalDate) : Date {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	fun areDatesOnSameDay(date1 : Date, date2 : Date) : Boolean {
		val fmt = SimpleDateFormat("yyyyMMdd")
		return fmt.format(date1) == fmt.format(date2)
	}
	
	fun isToday(date : LocalDate) : Boolean = areDatesOnSameDay(Date(), localDateToDate(date))

	fun isToday(date : Date) : Boolean = areDatesOnSameDay(Date(), date)

	fun timeStampInXHours(hours: Int): Timestamp {
		val ldt : LocalDateTime = LocalDateTime.ofInstant(Date().toInstant(), ZoneId.systemDefault())
		return Timestamp(Date.from(ldt.atZone(ZoneId.systemDefault()).plusHours(hours.toLong()).toInstant()).time)
	}
	
	fun addHours(date : Date, hours : Int) : Date {
		val ldt : LocalDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
		return Date.from(ldt.atZone(ZoneId.systemDefault()).plusHours(hours.toLong()).toInstant())
	}
	
	fun inXdays(date : Date, days : Int) : Timestamp {
		val ldt : LocalDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
		return Timestamp(Date.from(ldt.atZone(ZoneId.systemDefault()).plusDays(days.toLong()).toInstant()).time)
	}
	
	fun today() : Timestamp {
		 return inXdays(Date(), 0)
	}
	
	fun nextDay(date : Date) : Date  = addHours(date, 24)
		
	fun countMinutesBetween(date1 : Date, date2 : Date) : Int {
		val minutes : Long = ((date2.time - date1.time) / 1000) / 60
		return minutes.toInt()
	}
	
	fun countDaysBetween(date1 : Timestamp, date2 : Timestamp) : Int {
		val days : Long = ((date2.time - date1.time) / 1000) / 60 / 60 / 24
		return days.toInt()
	}
	
	fun toString(date : Date) : String  = SimpleDateFormat("yyyy-MM-dd").format(date)

	fun toString(date : LocalDate) : String  = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

	fun toTimeString(date : Date) : String  = SimpleDateFormat("dd MMMMM yyyy, H:m").format(date)

	fun parseLocalDate(date : String) : LocalDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)

	fun localDateToTimestamp(localDate : LocalDate) : Timestamp {
		return Timestamp.valueOf(localDate.atStartOfDay())
	}

	fun getHours(date : Date) : Int = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).hour

	fun getMinutesOverHalfHour(date : Date) = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).minute > 25

	fun getDate(year : Int, month : Int, day : Int) : Date = localDateToDate(LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(day))
}