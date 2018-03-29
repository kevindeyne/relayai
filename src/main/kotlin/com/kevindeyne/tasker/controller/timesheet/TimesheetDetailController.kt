package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.domain.TimesheetDayListing
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.TimesheetRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TimesheetDetailController(val timesheetRepository : TimesheetRepository, val issueRepository : IssueRepository) {
	
	companion object {
		const val TIME_GET_DAY = "/timesheet/dayIssues/{day}"
	}

	val tU = TimeUtils.INSTANCE

	@GetMapping(TIME_GET_DAY)
	fun getTimesheetDayIssues(model : Model, @PathVariable day : String) : List<TimesheetDayListing> {
		val u = SecurityHolder.getUserId()
		return issueRepository.getIssueList(u, tU.parseLocalDate(day))
	}
}