package com.kevindeyne.tasker

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.controller.timesheet.TimesheetParser
import com.kevindeyne.tasker.domain.TimesheetEntry
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.util.Date

class TimesheetTests {
	
	val parser = TimesheetParser.INSTANCE
	val time = TimeUtils.INSTANCE
	
	@Test
	fun testAreDatesOnSameDay() {
		val today = toDate(LocalDateTime.now())
		val yesterday = toDate(LocalDateTime.now().minusDays(1))
		
		Assert.assertFalse(time.areDatesOnSameDay(today, yesterday))
		Assert.assertTrue(time.areDatesOnSameDay(Date(1000), time.addHours(Date(1000), 4)))
		Assert.assertTrue(time.areDatesOnSameDay(Date(1000), Date(1100)))
	}

	@Test
	fun generateListingFromEntries_simple() {
		val today = toDate(LocalDateTime.now().withHour(14))
		val yesterday = toDate(LocalDateTime.now().withHour(14).minusDays(1))

		val entry1 = TimesheetEntry(yesterday, time.addHours(yesterday, 4))
		val entry2 = TimesheetEntry(today, time.addHours(today, 4))
		
		val entries = listOf(entry1, entry2)
		val result = parser.convertEntriesToListings(entries)
		
		Assert.assertTrue(result.isNotEmpty())
		Assert.assertEquals(2, result.size)
	}
	
	fun toDate(localDate : LocalDateTime) : Date = TimeUtils.INSTANCE.localDateToDate(localDate)
	
	//@Test
	fun generateTimeForStart() {

	}
		
	//@Test
	fun generateTimeForEnd() {
		
	}
}
