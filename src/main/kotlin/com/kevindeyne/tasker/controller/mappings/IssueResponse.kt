package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator
import com.kevindeyne.tasker.domain.CommentListing

data class IssueResponse @JsonCreator constructor(
		var id: Long = -1,
		var title: String = "",
		var descr: String = "",
		var status: String = "",
		var urgency: String = "",
		var impact: String = "",		
		var fixVersion: String = "",
		var creator: String = "",
		var createDate: String = "",
		var slaStatus: String = "",
		var comments: List<CommentListing> = listOf()
)