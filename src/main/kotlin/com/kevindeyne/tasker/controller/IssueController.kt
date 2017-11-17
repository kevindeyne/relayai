package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.amq.AMQMessage
import com.kevindeyne.tasker.amq.AMQMessageType
import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.IssueForm
import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IssueController(var issueRepository : IssueRepository, var jmsTemplate : JmsTemplate) {
	
	companion object {
		const val ISSUE_POST = "/issue/{id}"
	}
	
	@PostMapping(ISSUE_POST)
	fun createOrEditIssue(@RequestBody form : IssueForm) : FormResponse {
		//validate serverside
		var errors : Map<String, String>  = form.validate()
		if(errors.isEmpty()){
			//persist to DB (async? via queue)			
			jmsTemplate.convertAndSend("mailbox", AMQMessage(AMQMessageType.ISSUE_CREATE_OR_EDIT, "Hello world"))
			
			//add to websocket
			
			
			return FormResponse(status = "OK")
		} else{
			//do something with the errors
			return FormResponse(status = "INVALID")
		}				
	}
	
}