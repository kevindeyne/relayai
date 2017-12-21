package com.kevindeyne.tasker

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.controller.timesheet.TimesheetParser
import com.kevindeyne.tasker.domain.TimesheetEntry
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import com.kevindeyne.tasker.domain.TimesheetDay

class TimesheetTests {
	
	val parser = TimesheetParser.INSTANCE
	val time = TimeUtils.INSTANCE
	
	val format = SimpleDateFormat("dd/MM/yyyy")
		
	@Test
	fun testGenerateDays() {
		val today = toDate(LocalDateTime.now().withHour(14))
		val yesterday = toDate(LocalDateTime.now().withHour(14).minusDays(1))

		val entry1 = TimesheetEntry(yesterday, time.addHours(yesterday, 4))
		val entry2 = TimesheetEntry(today, time.addHours(today, 4))
		
		val days = parser.getTimesheetDays(listOf(entry1, entry2))
		
		//println(days)
		
		Assert.assertTrue(days.get(0).days.size == 7)
	}
	
		
	@Test
	fun testFirstSunday() {
		val d26nov2017 = time.getDate(2017, 11, 26)
		val d20dec2017 = time.getDate(2017, 12, 20)
		
		Assert.assertEquals(format.format(d26nov2017), format.format(time.localDateToDate(parser.determineStartDate(d20dec2017))))
	}
				
	@Test
	fun testFirstSundayOnSunday() {
		val d1octOnSun = time.getDate(2017, 10, 1)
		
		Assert.assertEquals(format.format(d1octOnSun), format.format(time.localDateToDate(parser.determineStartDate(d1octOnSun))))
	}
	
	@Test
	fun testLastSaturday() {		
		val d6jan2018 = time.getDate(2018, 1, 6)
		val d20dec2017 = time.getDate(2017, 12, 20)
		
		Assert.assertEquals(format.format(d6jan2018), format.format(time.localDateToDate(parser.determineEndDate(d20dec2017))))
	}
	
	@Test
	fun testLastSaturdayOnSaturday() {
		val d30sept2017OnSat = time.getDate(2017, 9, 30)
		
		Assert.assertEquals(format.format(d30sept2017OnSat), format.format(time.localDateToDate(parser.determineEndDate(d30sept2017OnSat))))
	}
	
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
		//println()
		//println(result)
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
		//println()
		//println(result)
		Assert.assertTrue(result.isNotEmpty())
		Assert.assertEquals(2, result.size)
	}
	
	@Test
	fun generateListingFromEntries_parallel() {
		val today = toDate(LocalDateTime.now().withHour(14).withMinute(15))
		val today2 = toDate(LocalDateTime.now().withHour(16).withMinute(15))

		val entry1 = TimesheetEntry(today, time.addHours(today, 3))
		val entry2 = TimesheetEntry(today2, time.addHours(today2, 3))
		
		val entries = listOf(entry1, entry2)
		val result = parser.getTimesheetDays(entries)
		
		Assert.assertTrue(result.isNotEmpty())
		
		var hasFilledDay = false
		for(week in result){
			for(day in week.days) {
				if(day.today){
					hasFilledDay = true
					Assert.assertEquals("5h", day.dayTotal)
					break
				}
			}
		}
		
		Assert.assertTrue(hasFilledDay)
	}

	@Test
	fun generateListingFromEntries_parallel2() {
		val today = toDate(LocalDateTime.now().withHour(14).withMinute(15))
		
		Assert.assertFalse(time.getMinutesOverHalfHour(today))
		Assert.assertTrue(time.getMinutesOverHalfHour(toDate(LocalDateTime.now().withHour(14).withMinute(31))))
		
		val today2 = toDate(LocalDateTime.now().withHour(15).withMinute(15))

		val entry1 = TimesheetEntry(today, time.addHours(today, 3))
		val entry2 = TimesheetEntry(today2, time.addHours(today2, 3))
		
		val entries = listOf(entry1, entry2)
		val result = parser.getTimesheetDays(entries)
		
		Assert.assertTrue(result.isNotEmpty())
		
		var hasFilledDay = false
		for(week in result){
			for(day in week.days) {
				if(day.today){
					hasFilledDay = true
					Assert.assertEquals("4h", day.dayTotal)
					break
				}
			}
		}
		
		Assert.assertTrue(hasFilledDay)
	}
	
	@Test
	fun generateListingFromEntries_parallel3() {
		val today = toDate(LocalDateTime.now().withHour(6).withMinute(15))
		val today2 = toDate(LocalDateTime.now().withHour(7).withMinute(15))
		val today3 = toDate(LocalDateTime.now().withHour(7).withMinute(15))
		val today4 = toDate(LocalDateTime.now().withHour(8).withMinute(15))
		val today5 = toDate(LocalDateTime.now().withHour(9).withMinute(15))

		val entry1 = TimesheetEntry(today, time.addHours(today, 5))
		val entry2 = TimesheetEntry(today2, time.addHours(today2, 5))
		val entry3 = TimesheetEntry(today3, time.addHours(today3, 5))
		val entry4 = TimesheetEntry(today4, time.addHours(today4, 5))
		val entry5 = TimesheetEntry(today5, time.addHours(today5, 5))
		
		val entries = listOf(entry1, entry2, entry3, entry4, entry5)
		val result = parser.getTimesheetDays(entries)
		
		Assert.assertTrue(result.isNotEmpty())
		
		var hasFilledDay = false
		for(week in result){
			for(day in week.days) {
				if(day.today){
					hasFilledDay = true
					Assert.assertEquals("8h", day.dayTotal)
					break
				}
			}
		}
		
		Assert.assertTrue(hasFilledDay)
	}
	
	fun toDate(localDate : LocalDateTime) : Date = TimeUtils.INSTANCE.localDateToDate(localDate)
	
	//@Test
	fun generateTimeForStart() {

	}
		
	//@Test
	fun generateTimeForEnd() {
		
	}
}
