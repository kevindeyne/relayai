package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class CommentForm @JsonCreator constructor(
		val text: String
){
	
	fun validate() : Map<String, String> = HashMap<String, String>()	
	
}

 