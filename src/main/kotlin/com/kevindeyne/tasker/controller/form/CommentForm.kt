package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator
import org.apache.commons.lang.StringEscapeUtils

data class CommentForm @JsonCreator constructor(
		val text: String
){
	fun validate() : Map<String, String> = HashMap<String, String>()
	
	fun toText() : String {
		return StringEscapeUtils.escapeHtml(text.replace("(\r\n|\n)".toRegex(), "[6s4br8aw]")).replace("[6s4br8aw]", "<br/>")
	}
	
}

 