package com.kevindeyne.tasker.controller.mappings

import com.fasterxml.jackson.annotation.JsonCreator
import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.domain.CommentListing

data class PullUpdate @JsonCreator constructor(
		var newIssues : List<IssueResponse> = listOf(),
		var comments : List<CommentListing> = listOf())