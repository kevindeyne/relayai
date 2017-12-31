package com.kevindeyne.tasker.domain

import com.kevindeyne.tasker.repositories.IssueRepositoryImpl

data class IssueListing constructor(
		val id: Long,
		val title: String,
		val shortDescr: String,
		val longDescr: String,
		val clazz: String = "",
		val importance: Int = IssueRepositoryImpl.IMPORTANCE_NORMAL
)  { }