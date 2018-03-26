package com.kevindeyne.tasker.domain

import java.util.Date

data class TimesheetDay constructor(
		var day : String,
		var dayName : String,
		var today : Boolean,
		var inactive : Boolean,
		var dayTotal : String,
		var issueTotal : String,
		var listings: List<TimesheetListing>
)  { }