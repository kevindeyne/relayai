package com.kevindeyne.tasker.controller.mappings

import com.fasterxml.jackson.annotation.JsonCreator
import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.domain.CommentListing

data class PullUpdate @JsonCreator constructor(
		var updateIssues : List<IssueResponse> = listOf(),
		var removeIssues : List<String> = listOf(),
		var comments : List<CommentListing> = listOf(),
		val myIssueCounter : Int = 0,
		val sprintCounter : Int = 0,
		val backlogCounter : Int = 0
)