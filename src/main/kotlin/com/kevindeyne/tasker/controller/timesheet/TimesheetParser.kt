package com.kevindeyne.tasker.controller.timesheet

import com.kevindeyne.tasker.domain.TimesheetDay
import com.kevindeyne.tasker.domain.TimesheetEntry
import com.kevindeyne.tasker.domain.TimesheetListing
import com.kevindeyne.tasker.domain.TimesheetWeek
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale

enum class TimesheetParser() {
	
	INSTANCE;
	
	val tU = TimeUtils.INSTANCE
	val format = SimpleDateFormat("yyyyMMdd")
	
	fun getTimesheetDays(entries : List<TimesheetEntry>) : List<TimesheetWeek> {
		val listings = convertEntriesToListings(entries)
		
		val startDate = determineStartDate()		
		val dayRange = ChronoUnit.DAYS.between(startDate, determineEndDate())
		
		val result : MutableList<TimesheetWeek> = mutableListOf()
		var days : MutableList<TimesheetDay> = mutableListOf()
		for(i in 0..dayRange){
			val date = startDate.plusDays(i)
			val dateAsCompareString = format.format(tU.localDateToDate(date))
			
			var totalInHours = 0.toDouble();
			
			val slots : MutableMap<Int, Boolean> = mutableMapOf()
			val dayListing : MutableList<TimesheetListing> = mutableListOf()
			listings.forEach{ t -> 
				if(dateAsCompareString.equals(t.dateString)) {
					markSlotsAsBusy(slots, tU.getHours(t.startDate), tU.getMinutesOverHalfHour(t.startDate), tU.getHours(t.endDate), tU.getMinutesOverHalfHour(t.endDate))
					dayListing.add(t)
				}
			}
			
			for(value in slots.values){
		        if(value){ totalInHours += 0.5.toDouble() }
		    }
									
			days.add(TimesheetDay(date.getDayOfMonth().toString(),
						date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.UK),
						tU.isToday(date),
						!date.getMonthValue().equals(LocalDate.now().getMonthValue()),
						DecimalFormat("##.##").format(totalInHours) + "h",
						dayListing))
			
			if(days.size == 7){
				result.add(TimesheetWeek(days))
				days = mutableListOf()
			}
		}
		
		return result
	}
	
	fun markSlotsAsBusy(list : MutableMap<Int, Boolean>, startSlot : Int, startPassedHalf : Boolean, endSlot : Int, endPassedHalf : Boolean) {
		var start = startSlot*2
		var end = (endSlot*2)-1
		
		if(startPassedHalf){ start++ }
		if(endPassedHalf){ end++ }
		
		for(i in start..end){
			list.put(i, true)
		}
	}
		
	fun determineStartDate(date : Date = Date()) : LocalDate {
		val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		
		if(DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek())){
			return localDate
		} else {
			val firstSunday = localDate.withDayOfMonth(1).with( TemporalAdjusters.previous( DayOfWeek.SUNDAY ) )
			return firstSunday
		}
	}
	
	fun determineEndDate(date : Date = Date()) : LocalDate {
		val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		if(DayOfWeek.SATURDAY.equals(localDate.getDayOfWeek())){
			return localDate			
		} else {
			val lastSaturday = localDate.withDayOfMonth(localDate.lengthOfMonth()).with( TemporalAdjusters.next( DayOfWeek.SATURDAY ) )
			return lastSaturday
		}
	}
		
	fun convertEntriesToListings(entries : List<TimesheetEntry>) : List<TimesheetListing> {

		val timesheetMap : MutableMap<Date, List<TimesheetEntry>> = HashMap<Date, List<TimesheetEntry>>()		
		entries.stream().forEach{
			t -> addList(t, timesheetMap)
		}
		
		val result : MutableList<TimesheetListing> = mutableListOf()
		timesheetMap.keys.stream().forEach {
			key -> convertListToListings(result, timesheetMap, key)
		}
		
		return result
	}
	
	fun addList(entry : TimesheetEntry, map : MutableMap<Date, List<TimesheetEntry>>) = addListWithDatecheck(entry, map, entry.startDate)
	
	fun addListWithDatecheck(entry : TimesheetEntry, map : MutableMap<Date, List<TimesheetEntry>>, date : Date) {
		if(!tU.areDatesOnSameDay(date, entry.endDate)){
			val hoursADay = entry.avgWorkday
			val endDate = tU.addHours(date, hoursADay)
			addEntryToMap(entry.cloneWithEnddate(date, endDate), map, date)

			addListWithDatecheck(entry, map, tU.nextDay(entry.startDate))
		} else {
			addEntryToMap(entry.cloneWithEnddate(date, entry.endDate), map, date)
		}
	}
	
	fun addEntryToMap(entry : TimesheetEntry, map : MutableMap<Date, List<TimesheetEntry>>, date : Date){
		val e = map.get(date)
		if(e != null) {
			val entryList = e.toMutableList()
			entryList.add(entry)
			map.put(date, entryList)
		} else {
			map.put(date, listOf(entry))
		}
	}
	
	fun convertListToListings(result : MutableList<TimesheetListing>, map : MutableMap<Date, List<TimesheetEntry>>, key : Date){
		val list = map.get(key)
		if(list != null){
			for (entry: TimesheetEntry in list) {
				val minutes = tU.countMinutesBetween(entry.startDate, entry.endDate)
				if(minutes > 2) {
				  	result.add(TimesheetListing(
				  		key,
						format.format(key),
						entry.issueName,
						entry.issueId,
						entry.startDate,
						entry.endDate))
					}
			}
		}
	}
}