package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TimesheetController() {
	
	companion object {
		const val TIME_GET = "/timesheet"
	}
	
	@GetMapping(TIME_GET)
	fun getTimesheet(model : Model) : String {
		model.addAttribute("activePage", "timesheet");
		return "timesheet"
	}	
}