package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class IssueForm @JsonCreator constructor(
		val title: String,
		val description: String,
		val project: String
){
	
	fun validate() : Map<String, String> = HashMap<String, String>()	
	
}

 