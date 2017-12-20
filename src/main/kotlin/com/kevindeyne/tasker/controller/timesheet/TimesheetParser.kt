package com.kevindeyne.tasker.controller.timesheet

import com.kevindeyne.tasker.domain.TimesheetEntry
import com.kevindeyne.tasker.domain.TimesheetListing
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

enum class TimesheetParser() {
	
	INSTANCE;
	
	val tU = TimeUtils.INSTANCE
		
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
			val totalDay = calculateTotal(list)
			
			for (entry: TimesheetEntry in list) {
				val minutes = tU.countMinutesBetween(entry.startDate, entry.endDate)
				if(minutes > 2) {
					val hours = minutes / 60
					
				  	result.add(TimesheetListing(key, tU.toString(key),
						totalDay,
						entry.issueName,
						entry.issueId,
						hours.toString()))
					}
			}
		}
	}
	
	fun calculateTotal(list : List<TimesheetEntry>) : String = list.sumBy { tU.countMinutesBetween(it.startDate, it.endDate) }.div(60).toString()
}