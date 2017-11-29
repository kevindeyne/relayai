package com.kevindeyne.tasker.amq

import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.TagcloudRepository
import com.kevindeyne.tasker.service.KeywordGeneration
import org.springframework.context.annotation.DependsOn
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@DependsOn("AMQConfig")
@Component
class GlobalReceiver(val issueRepository: IssueRepository, val tagcloud: TagcloudRepository) {

	@JmsListener(destination = "issues", containerFactory = "jmsFactory")
	fun onMessage(message: AMQMessage) {
		when (message.type) {
			AMQMessageType.ISSUE_CREATE_OR_EDIT -> handleCreateOrEditIssue(message)
			AMQMessageType.ISSUE_TAGCLOUD -> handleTagcloudGeneration(message)
			else -> println("Unknown message type")
		}
	}

	fun handleCreateOrEditIssue(message: AMQMessage) {
		if ("new".equals(message.id)) {
			val title = message.value
			val description = "This is a new issue and this is the description of it."
			issueRepository.create(title, description, message.userId, message.sprintId, message.projectId)
		} else {
			//edit TODO
		}
	}

	fun handleTagcloudGeneration(msg: AMQMessage) = KeywordGeneration.generateKeywords(msg.value).forEach { k -> tagcloud.addToIssueIfNotExists(k, msg.issueId) }

}