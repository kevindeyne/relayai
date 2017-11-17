package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.IssueForm
import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IssueController(var issueRepository : IssueRepository) {
	
	companion object {
		const val ISSUE_POST = "/issue/{id}"
	}
	
	@PostMapping(ISSUE_POST)
	fun createOrEditIssue(@RequestBody form : IssueForm) : FormResponse {
		//validate serverside
		var errors : Map<String, String>  = form.validate()
		if(errors.isEmpty()){
			//persist to DB (async? via queue)
			//add to websocket
			return FormResponse(status = "OK")
		} else{
			//do something with the errors
			return FormResponse(status = "INVALID")
		}				
	}
	
}