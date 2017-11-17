package com.kevindeyne.tasker.amq

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class GlobalReceiver {
	
	@JmsListener(destination = "mailbox", containerFactory = "myFactory")
	fun onMessage(message : AMQMessage){		
		println(message)
		println(message.type)
		println(message.value)
		
	}
}