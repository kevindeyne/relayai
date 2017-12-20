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

	@Test
	fun generateListingFromEntries_simple() {
		val today = toDate(LocalDateTime.now().withHour(14))
		val yesterday = toDate(LocalDateTime.now().withHour(14).minusDays(1))

		val entry1 = TimesheetEntry(yesterday, time.addHours(yesterday, 4))
		val entry2 = TimesheetEntry(today, time.addHours(today, 4))
		
		val entries = listOf(entry1, entry2)
		val result = parser.convertEntriesToListings(entries)
		val result2 = parser.convertEntriesToListings(entries)
		Assert.assertTrue(result.isNotEmpty())
		Assert.assertEquals(2, result.size)
		
		Assert.assertTrue(result2.isNotEmpty())
		Assert.assertEquals(2, result2.size)
	}
	
	@Test
	fun generateListingFromEntries_crossDay_1() {
		val today = toDate(LocalDateTime.now().withHour(8))

		val entry = TimesheetEntry(today, time.nextDay(today))
		
		//expected 0800-1600
		//+ 	   0800-0800 (ignore)
	
		val result = parser.convertEntriesToListings(listOf(entry))
		println()
		println(result)
		Assert.assertTrue(result.isNotEmpty())
		Assert.assertEquals(1, result.size)
	}
	
	@Test
	fun generateListingFromEntries_crossDay_2() {
		val today = toDate(LocalDateTime.now().withHour(8))

		val entry = TimesheetEntry(today, time.addHours(time.nextDay(today), 8))
		
		//expected 0800-1600
		//+ 	   0800-1600
	
		val result = parser.convertEntriesToListings(listOf(entry))
		println()
		println(result)
		Assert.assertTrue(result.isNotEmpty())
		Assert.assertEquals(2, result.size)
	}
	
	//TODO @Test
	fun generateListingFromEntries_parallel() {
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
