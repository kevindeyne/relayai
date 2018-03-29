package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.controller.timesheet.TimesheetParser
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.TimesheetRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

@Controller
class TimesheetController(val timesheetRepository : TimesheetRepository, val issueRepository : IssueRepository) {
	
	companion object {
		const val TIME_GET = "/timesheet"
	}

	val tP = TimesheetParser.INSTANCE
	val tU = TimeUtils.INSTANCE
	
	@GetMapping(TIME_GET)
	fun getTimesheet(model : Model) : String {
		model.addAttribute("activePage", "timesheet")
		val detailDate = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		getTimesheetInfo(model, detailDate, null, null)
		return "timesheet"
	}
	
	fun getTimesheetInfo(model : Model, detailDate : LocalDate, startDate : LocalDate?, endDate : LocalDate?){
		val u = SecurityHolder.getUserId()

		val startPeriod = startDate ?: tP.determineStartDate()
		val endPeriod = endDate ?: tP.determineEndDate()

		val timesheets = timesheetRepository.getTimesheet(tU.localDateToDate(startPeriod), tU.localDateToDate(endPeriod), u)
		model.addAttribute("timesheets", tP.getTimesheetDays(timesheets, startPeriod, endPeriod))
		model.addAttribute("dayIssues", issueRepository.getIssueList(u, detailDate))
		
		val month = detailDate.month.getDisplayName(TextStyle.FULL, Locale.UK)
		model.addAttribute("currentMonth", month)
		model.addAttribute("currentDay", "$month ${detailDate.dayOfMonth}")
	}
}