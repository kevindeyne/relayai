package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class IssueResponse @JsonCreator constructor(
		var id: Long,
		var title: String,
		var descr: String,
		var status: String,
		var urgency: String,
		var impact: String,		
		var fixVersion: String,
		var creator: String,
		var createDate: String,
		var slaStatus: String		
)