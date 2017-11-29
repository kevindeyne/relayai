package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.controller.form.StandupResponse
import com.kevindeyne.tasker.domain.IssueListing

interface TagcloudRepository {
	fun addToIssueIfNotExists(keyword: String, issueId: Long?)
}