package com.kevindeyne.tasker.domain

data class ProjectVersion constructor(
		var majorVersion: Int = 0,
		var minorVersion: Int = 0,
		var patchVersion: Int = 0
) {
}