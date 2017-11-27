package com.kevindeyne.tasker.amq

import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.context.annotation.DependsOn
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@DependsOn("AMQConfig")
@Component
class GlobalReceiver(var issueRepository : IssueRepository) {
	
	@JmsListener(destination = "issues", containerFactory = "jmsFactory")
	fun onMessage(message : AMQMessage){
		if(AMQMessageType.ISSUE_CREATE_OR_EDIT.equals(message.type)){
			if("new".equals(message.id)){
								
				val title = message.value
				val description = "This is a new issue and this is the description of it."
				val userId = message.userId
				val sprintId = message.sprintId
				val projectId = message.projectId
					
				issueRepository.create(title, description, userId, sprintId, projectId)
			} else {
				//edit TODO				
			}
		}
			
		//TODO debug logger println(message)
		
	}
}