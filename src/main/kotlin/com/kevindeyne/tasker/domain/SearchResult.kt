package com.kevindeyne.tasker.domain

data class SearchResult constructor(
		val id: Long,
		val linkedId: Long,
		val type: SearchResultType,
		val name: String
)  { }