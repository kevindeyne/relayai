package com.kevindeyne.tasker.amq

data class AMQMessage (var type : AMQMessageType, var value : String)