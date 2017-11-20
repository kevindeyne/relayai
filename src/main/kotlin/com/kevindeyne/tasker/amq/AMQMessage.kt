package com.kevindeyne.tasker.amq

data class AMQMessage (var id : String, var type : AMQMessageType, var value : String)