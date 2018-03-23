package com.kevindeyne.tasker.amq

data class AMQMessage @JvmOverloads constructor(val id: String, val type: AMQMessageType, val value: String, val value2: String = "", val description: String = "", val userId: Long, val sprintId: Long, val projectId: Long, val issueId: Long? = -1L)