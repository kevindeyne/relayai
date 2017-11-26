package com.kevindeyne.tasker.amq

import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.context.annotation.DependsOn
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@DependsOn("AMQConfig")
@Component
class GlobalReceiver(var issueRepository : IssueRepository) {
	
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	fun onMessage(message : AMQMessage){
		if(AMQMessageType.ISSUE_CREATE_OR_EDIT.equals(message.type)){
			if("new".equals(message.id)){
				issueRepository.create(message.value, "This is a new issue and this is the description of it.");
			} else {
				//edit
				
			}
		}
			
		println(message)
		//println(message.type)
		//println(message.value)
		
	}
}