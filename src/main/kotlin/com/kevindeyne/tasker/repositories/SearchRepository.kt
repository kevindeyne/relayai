package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.SearchResult

interface SearchRepository {
	
	fun findInSrcVal(text : String) : List<SearchResult>
	
}