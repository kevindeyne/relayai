package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.amq.AMQMessage
import com.kevindeyne.tasker.amq.AMQMessageType
import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.IssueForm
import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class IssueController(var issueRepository : IssueRepository, var jmsTemplate : JmsTemplate) {
	
	companion object {
		const val ISSUE_DETAIL = "/issue/{id}"
	}
		
	@PostMapping(ISSUE_DETAIL)
	fun createOrEditIssue(@RequestBody form : IssueForm, @PathVariable id : String) : FormResponse {
		var errors : Map<String, String>  = form.validate()
		if(errors.isEmpty()){
			jmsTemplate.convertAndSend("issues", AMQMessage(id, AMQMessageType.ISSUE_CREATE_OR_EDIT, "Hello world"))
			//add to pulling notification table
			return FormResponse(status = "OK")
		} else{
			//do something with the errors (set them in formresponse)
			return FormResponse(status = "INVALID")
		}				
	}
	
	@GetMapping(ISSUE_DETAIL)
	fun getIssue(@PathVariable id : String) : IssueResponse {
		//validate access
		
		val reponse = issueRepository.findById(id.toLong())		
		if (reponse == null){
			return IssueResponse(-1, "", "")
		}
		return reponse 
	}	
}