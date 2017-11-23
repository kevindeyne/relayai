package com.kevindeyne.tasker.controller.mappings

import com.fasterxml.jackson.annotation.JsonCreator
import com.kevindeyne.tasker.controller.form.IssueResponse

data class PullUpdate @JsonCreator constructor(var newIssues : List<IssueResponse>)