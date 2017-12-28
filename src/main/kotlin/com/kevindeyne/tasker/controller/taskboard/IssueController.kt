package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.amq.AMQMessage
import com.kevindeyne.tasker.amq.AMQMessageType
import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.IssueForm
import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.apache.commons.lang3.StringUtils

@RestController
class IssueController(var issueRepository : IssueRepository, var jmsTemplate : JmsTemplate) {
	
	companion object {
		const val ISSUE_DETAIL = "/issue/{id}"
		const val ISSUE_PROGRESS = ISSUE_DETAIL + "/{action}/{changedValue}"
	}
		
	@PostMapping(ISSUE_DETAIL)
	fun createOrEditIssue(@RequestBody form : IssueForm, @PathVariable id : String) : FormResponse {
		var errors : Map<String, String>  = form.validate()
		if(errors.isEmpty()){
			
			val userId = SecurityHolder.getUserId()
			val sprintId = SecurityHolder.getSprintId()
			val projectId = SecurityHolder.getProjectId()
			
			if(userId == null || sprintId == null || projectId == null){
				return FormResponse(status = "INVALID")
			}
			
			val message = AMQMessage(id, AMQMessageType.ISSUE_CREATE_OR_EDIT, "Hello world", userId, sprintId, projectId)			
			jmsTemplate.convertAndSend("issues", message)
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
			return IssueResponse()
		}
		return reponse 
	}
	
	@PostMapping(ISSUE_PROGRESS)
	fun progressIssue(@PathVariable id : String, @PathVariable action : String, @PathVariable changedValue : String) : FormResponse {
		val userId = SecurityHolder.getUserId()
		val sprintId = SecurityHolder.getSprintId()
		val projectId = SecurityHolder.getProjectId()
		
		if(userId == null || sprintId == null || projectId == null){
			return FormResponse(status = "INVALID")
		}
				
		val message = AMQMessage(id, determineMessageType(action), changedValue, userId, sprintId, projectId)
		jmsTemplate.convertAndSend("issues", message)
		//add to pulling notification table
		return FormResponse(status = "OK")
	}
	
	fun determineMessageType(action : String) : AMQMessageType{
		when(action){
			"progress" -> return AMQMessageType.ISSUE_PROGRESS
			"impact" -> return AMQMessageType.ISSUE_IMPACT
			"urgency" -> return AMQMessageType.ISSUE_URGENCY
			"fixversion" -> return AMQMessageType.ISSUE_FIXVERSION
			"assignee" -> return AMQMessageType.ISSUE_ASSIGNEE
		}
		
		throw RuntimeException("Could not determine message type in IssueController")
	}
}