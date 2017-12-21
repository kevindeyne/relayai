package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.domain.TimesheetEntry
import com.kevindeyne.tasker.repositories.TimesheetRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import com.kevindeyne.tasker.controller.timesheet.TimesheetParser

@Controller
class TimesheetController(val timesheetRepository : TimesheetRepository) {
	
	companion object {
		const val TIME_GET = "/timesheet"
	}
	
	@GetMapping(TIME_GET)
	fun getTimesheet(model : Model) : String {
		model.addAttribute("activePage", "timesheet")
		getTimesheetInfo(model)
		return "timesheet"
	}
	
	fun getTimesheetInfo(model : Model){
		val timesheets = timesheetRepository.getTimesheetForSprint(SecurityHolder.getUserId(), SecurityHolder.getSprintId())
		model.addAttribute("timesheets", TimesheetParser.INSTANCE.convertEntriesToListings(timesheets))
	}
}