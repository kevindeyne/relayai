package com.kevindeyne.tasker.domain

import java.util.Date

data class TimesheetListing constructor(
		var date : Date,
		var dateString : String,
		var dateTotal: String,
		var issueName: String,
		var issueId: Long,
		var duration: String
)  { }