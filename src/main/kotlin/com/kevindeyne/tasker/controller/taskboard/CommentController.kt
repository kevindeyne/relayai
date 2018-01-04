package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.amq.AMQMessage
import com.kevindeyne.tasker.amq.AMQMessageType
import com.kevindeyne.tasker.controller.form.CommentForm
import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.repositories.CommentRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.kevindeyne.tasker.domain.CommentListing

@RestController
class CommentController(var commentRepository : CommentRepository, var jmsTemplate : JmsTemplate) {
	
	companion object {
		const val COMMENT_DETAIL = "/comment/{issueId}"
	}
		
	@PostMapping(COMMENT_DETAIL)
	fun createOrEditIssue(@RequestBody form : CommentForm, @PathVariable issueId : String) : CommentListing {
		var errors : Map<String, String>  = form.validate()
		if(errors.isEmpty()){
			
			val userId = SecurityHolder.getUserId()			
			val newListing = commentRepository.createComment(form.text, issueId.toLong(), userId);
			
			//add to pulling notification table
			return newListing
		} else{
			//do something with the errors (set them in formresponse)
			return CommentListing()
		}				
	}
}