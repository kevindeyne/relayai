package com.kevindeyne.tasker.domain

data class TimesheetDay constructor(
		var day : String,
		var dayName : String,
		var today : Boolean,
		var inactive : Boolean,
		var total : Int
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
}