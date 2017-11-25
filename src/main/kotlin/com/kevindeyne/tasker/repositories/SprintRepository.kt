package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.domain.IssueListing
import java.sql.Timestamp

interface SprintRepository {	
	fun findById(issueId : Long) : IssueResponse
	
	fun findCurrentSprintId() : Long
	
	fun create(title : String, description : String)
}