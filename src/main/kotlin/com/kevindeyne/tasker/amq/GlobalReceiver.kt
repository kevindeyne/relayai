package com.kevindeyne.tasker.amq

import org.springframework.stereotype.Component

@Component
class GlobalReceiver {
	
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	fun onMessage(message : AMQMessage){
		
	}
}