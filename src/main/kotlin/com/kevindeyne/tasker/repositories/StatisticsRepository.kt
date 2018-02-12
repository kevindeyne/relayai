package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.Progress
import com.kevindeyne.tasker.domain.StatisticsListing

interface StatisticsRepository {
	
	fun getStats(sprintId : Long) : StatisticsListing

	fun getStatusCounts(sprintId : Long) : Map<String, Int> 
	
}