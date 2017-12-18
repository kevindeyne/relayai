package com.kevindeyne.tasker.controller.form

import com.fasterxml.jackson.annotation.JsonCreator

data class SearchForm @JsonCreator constructor(
		val search: String
){
		
}

 