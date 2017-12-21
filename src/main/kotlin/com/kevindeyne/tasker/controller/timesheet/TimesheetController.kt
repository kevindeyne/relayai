package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.timesheet.TimesheetParser
import com.kevindeyne.tasker.repositories.TimesheetRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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
		model.addAttribute("timesheets", TimesheetParser.INSTANCE.getTimesheetDays(timesheets))
		
		model.addAttribute("currentMonth", LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.UK))
		
	}
}