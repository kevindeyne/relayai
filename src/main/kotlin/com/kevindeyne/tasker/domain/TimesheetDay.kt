package com.kevindeyne.tasker.domain

data class TimesheetDay constructor(
		var day : String,
		var dayName : String,
		var today : Boolean,
		var inactive : Boolean,
		var total : Int = 0,
		var hours : Double = 0.0
) {
	fun getIssueTotal() : String{
		if (total == 0) {
			return "None"
		} else if (total == 1){
			return "1 issue"
		} else {
			return "$total issues"
		}
	}

	fun getDayTotal() : String{
		if (hours == 0.0 && total > 0) {
			hours = 0.5
		}

		if(hours == 0.0) {
			return "-"
		} else {
			return "${hours}h"
		}

	}
}