package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class ProjectForm @JsonCreator constructor(
		val title: String = "",
		val newOrExisting: String = "",
		val releaseSchedule: String = "",
		val sprintFrequency: String = "",
		val existingVersion: String = ""
){
	
	fun validate() : Map<String, String> = HashMap<String, String>()	
	
}

 