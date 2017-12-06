package com.kevindeyne.tasker.amq

import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.KnowledgeRepository
import com.kevindeyne.tasker.repositories.TagcloudRepository
import com.kevindeyne.tasker.service.KeywordGeneration
import org.springframework.context.annotation.DependsOn
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@DependsOn("AMQConfig")
@Component
class GlobalReceiver(val issueRepository: IssueRepository, val tagcloud: TagcloudRepository, val knowledge: KnowledgeRepository) {

	@JmsListener(destination = "issues", containerFactory = "jmsFactory")
	fun onMessage(message: AMQMessage) {
		when (message.type) {
			AMQMessageType.ISSUE_CREATE_OR_EDIT -> handleCreateOrEditIssue(message)
			AMQMessageType.ISSUE_PROGRESS -> handleProgress(message)
			else -> println("Unknown message type")
		}
	}

	//TODO transactionality
	fun handleCreateOrEditIssue(message: AMQMessage) {
		val create = "new".equals(message.id)
		var issueId : Long
		
		val title = message.value
		val description = "This is a new issue and this is the description of it."
		
		if(create){
			issueId = issueRepository.create(title, description, message.userId, message.sprintId, message.projectId, message.userId)
		} else {
			issueId = message.id.toLong()
			issueRepository.update(issueId, title, description, message.userId)
		}
		
		handleTagcloudGeneration("$title. $description", issueId)
		
		if(create){
			println("Attempting auto-assignment ...")
			autoAssignment(issueId)
		}
	}

	fun handleTagcloudGeneration(msg: String, issueId : Long) = KeywordGeneration.generateKeywords(msg).forEach { k -> tagcloud.addToIssueIfNotExists(k, issueId) }
	
	fun autoAssignment(issueId : Long) {
		val userId = knowledge.findMostSuitedCandidateForIssue(issueId)
		println("Most suited : " + userId)
		if(userId != null){
			println("Assigning issue to " + userId)
			issueRepository.assign(issueId, userId)
		} else {
			println("Nobody to assign to, staying with current user")			
		}
	}
	
	fun handleProgress(message: AMQMessage){
		val status = Progress.valueOf(message.value)
		val issueId = message.id.toLong() 
		issueRepository.updateStatus(issueId, status)
		
		when(status){
			Progress.DONE -> handleSolved(issueId, message.userId)
		}
	}
	
	fun handleSolved(issueId : Long, userId : Long) {
		val tagcloud : List<Long> = tagcloud.findByIssues(issueId.toLong())
		tagcloud.forEach{ k -> knowledge.addToKnowledgeIfNotExists(k, userId) }
	}
}