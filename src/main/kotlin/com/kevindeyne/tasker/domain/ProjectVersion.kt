package com.kevindeyne.tasker.domain

data class ProjectVersion constructor(
		val majorVersion: Int = 0,
		val minorVersion: Int = 0,
		val patchVersion: Int = 0
) {
}