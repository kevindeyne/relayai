package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.amq.AMQMessage
import com.kevindeyne.tasker.amq.AMQMessageType
import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.IssueForm
import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.domain.IssueListing
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
class IssueController(var issueRepository : IssueRepository, var jmsTemplate : JmsTemplate) {
	
	companion object {
		const val ISSUE_DETAIL = "/issue/{id}"
		const val ISSUE_PROGRESS = ISSUE_DETAIL + "/{action}/{changedValue}"
		const val ISSUE_PROGRESS_W_BRANCH = ISSUE_DETAIL + "/{action}/{branch}/{version}"
		const val ISSUE_LIST_MYISSUES = "/issue/list/issue"
		const val ISSUE_LIST_TEAM = "/issue/list/team"
		const val ISSUE_LIST_BACKLOG = "/issue/list/backlog"
	}
	
	@PostMapping(ISSUE_DETAIL)
	fun createOrEditIssue(@RequestBody form : IssueForm, @PathVariable id : String) : FormResponse {
		var errors : Map<String, String>  = form.validate()
		if(errors.isEmpty()){
			
			val userId = SecurityHolder.getUserId()
			val sprintId = SecurityHolder.getSprintId()
			val projectId = SecurityHolder.getProjectId()
			
			if(sprintId == null){
				return FormResponse(status = "INVALID")
			}
			
			val message = AMQMessage(id, AMQMessageType.ISSUE_CREATE_OR_EDIT, form.title, "", form.description, userId, sprintId, projectId)			
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
		
		if(sprintId == null){
			return FormResponse(status = "INVALID")
		}
				
		val message = AMQMessage(id, determineMessageType(action), changedValue, "", "", userId, sprintId, projectId)
		jmsTemplate.convertAndSend("issues", message)
		//add to pulling notification table
		return FormResponse(status = "OK")
	}
	
	@PostMapping(ISSUE_PROGRESS_W_BRANCH)
	fun progressIssue(@PathVariable id : String, @PathVariable action : String, @PathVariable branch : String, @PathVariable version : String) : FormResponse {
		val userId = SecurityHolder.getUserId()
		val sprintId = SecurityHolder.getSprintId()
		val projectId = SecurityHolder.getProjectId() 
		
		if(sprintId == null){
			return FormResponse(status = "INVALID")
		}
				
		val message = AMQMessage(id, determineMessageType(action), version, branch, "", userId, sprintId, projectId, id.toLong())
		jmsTemplate.convertAndSend("issues", message)
		//add to pulling notification table
		return FormResponse(status = "OK")
	}
	
	@DeleteMapping(ISSUE_PROGRESS_W_BRANCH)
	fun deleteVersionFromIssue(@PathVariable id : String, @PathVariable action : String, @PathVariable branch : String, @PathVariable version : String) : FormResponse {
		val userId = SecurityHolder.getUserId()
		val sprintId = SecurityHolder.getSprintId()
		val projectId = SecurityHolder.getProjectId() 
		
		if(sprintId == null){
			return FormResponse(status = "INVALID")
		}
				
		val message = AMQMessage(id, AMQMessageType.ISSUE_REMOVE_VERSION, version, branch, "", userId, sprintId, projectId, id.toLong())
		jmsTemplate.convertAndSend("issues", message)
		//add to pulling notification table
		return FormResponse(status = "OK")
	}
	
	fun determineMessageType(action : String) : AMQMessageType{
		when(action){
			"progress" -> return AMQMessageType.ISSUE_PROGRESS
			"impact" -> return AMQMessageType.ISSUE_IMPACT
			"urgency" -> return AMQMessageType.ISSUE_URGENCY
			"version" -> return AMQMessageType.ISSUE_ADD_VERSION
			"assignee" -> return AMQMessageType.ISSUE_ASSIGNEE
		}
		
		throw RuntimeException("Could not determine message type in IssueController")
	}
	
	@GetMapping(ISSUE_LIST_MYISSUES)
	fun getIssueListMine() : List<IssueListing>  {
		return issueRepository.findAllActiveForUserInCurrentSprint()
	}
	
	@GetMapping(ISSUE_LIST_TEAM)
	fun getIssueListTeam() : List<IssueListing>  {
		val sprintId = SecurityHolder.getSprintId()
		if(sprintId != null){
			return issueRepository.findAllActiveForTeamInCurrentSprint(sprintId, SecurityHolder.getUserId())
		}
		return issueRepository.findAllActiveForUserInCurrentSprint()
	}
	
	@GetMapping(ISSUE_LIST_BACKLOG)
	fun getIssueListBacklog() : List<IssueListing>  {
		return issueRepository.findAllBacklogForProject(SecurityHolder.getProjectId())
	}
}