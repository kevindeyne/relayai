package com.kevindeyne.tasker.domain

data class StatisticsListing constructor(
		var sprintCompletionRate: Int = 0,
		var issueCompletionRate: Int = 0,
		var daysUntilRelease: Int = 0,
		var nextReleaseVersion: String = "0.0.1",
		var issuePlannedDone: Int = 0,
		var issuePlannedTotal: Int = 0,
		var issuePlannedActive: Int = 0,
		var waitingForFeedbackRate: Int = 0,
		var issuesWaitingForFeedback: Int = 0,
		var issuesAddedSinceSprintCreation: String = "0",
		var sprintNr: Int = 0,
		var sprintReleaseRate: String = "bi-weekly",
		var noReleasesSinceStartProject: Int = 0,
		var backlogIssuesAtSprintStart: Int = 0,
		var backlogIssuesTotal: Int = 0
) { }