package com.kevindeyne.tasker

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.controller.timesheet.TimesheetParser
import com.kevindeyne.tasker.domain.TimesheetEntry
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class TimesheetTests {
	
	val parser = TimesheetParser.INSTANCE
	val time = TimeUtils.INSTANCE
	
	val format = SimpleDateFormat("dd/MM/yyyy")
		
	@Test
	fun testGenerateDays() {
		val t = LocalDateTime.now().withHour(14)
		val y = LocalDateTime.now().withHour(2).minusDays(1)
		val today = toDate(t)
		val yesterday = toDate(y)

		val entry1 = TimesheetEntry(yesterday, time.addHours(yesterday, 4))
		val entry2 = TimesheetEntry(today, time.addHours(today, 4))
		
		val days = parser.getTimesheetDays(listOf(entry1, entry2), y.toLocalDate(), t.toLocalDate())

		Assert.assertTrue(days[0].days.size > 0)
		Assert.assertEquals(1, days[0].days[0].total)
		Assert.assertEquals(4.0, days[0].days[0].hours, 0.0)
		Assert.assertEquals(1, days[0].days[1].total)
		Assert.assertEquals(4.0, days[0].days[1].hours, 0.0)
	}

	@Test
	fun testAreDatesOnSameDay() {
		val today = toDate(LocalDateTime.now())
		val yesterday = toDate(LocalDateTime.now().minusDays(1))

		Assert.assertFalse(time.areDatesOnSameDay(today, yesterday))
		Assert.assertTrue(time.areDatesOnSameDay(Date(1000), time.addHours(Date(1000), 4)))
		Assert.assertTrue(time.areDatesOnSameDay(Date(1000), Date(1100)))

		val t = LocalDateTime.now().withHour(14);
		val y = LocalDateTime.now().withHour(2)
		val t2 = toDate(t)
		val y2 = toDate(y)

		val entry1 = TimesheetEntry(y2, time.addHours(y2, 4))
		val entry2 = TimesheetEntry(t2, time.addHours(t2, 4))

		val days = parser.getTimesheetDays(listOf(entry1, entry2), y.toLocalDate(), t.toLocalDate())

		Assert.assertTrue(days[0].days.size > 0)
		Assert.assertEquals(2, days[0].days[0].total)
		Assert.assertEquals(8.0, days[0].days[0].hours, 0.0)
	}
	
	@Test
	fun testMinuteCount() {
		val today = toDate(LocalDateTime.now().withHour(14).withMinute(0).withSecond(0))
		val todayPlusMinute = toDate(LocalDateTime.now().withHour(14).withMinute(1).withSecond(0))
		
		Assert.assertEquals(1, time.countMinutesBetween(today, todayPlusMinute))
	}
	
	@Test
	fun testMinuteCount2() {
		val today = toDate(LocalDateTime.now().withHour(14).withMinute(0).withSecond(0))
		val todayPlusMinute = toDate(LocalDateTime.now().withHour(15).withMinute(0).withSecond(0))
		
		Assert.assertEquals(60, time.countMinutesBetween(today, todayPlusMinute))
	}

	fun toDate(localDate : LocalDateTime) : Date = TimeUtils.INSTANCE.localDateToDate(localDate)

	//@Test
	fun generateTimeForStart() {

	}
		
	//@Test
	fun generateTimeForEnd() {
		
	}
}
