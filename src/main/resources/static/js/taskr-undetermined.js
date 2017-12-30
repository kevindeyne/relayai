$(document).ready(function() {
	$("#ud-plan").click(function() {
		var undeterminedPlanUrl = "/undetermined/{issueId}/plan/{workload}";
		undeterminedPlanUrl = undeterminedPlanUrl.replace("{issueId}", $("aside section.active").attr("issue-id"));
		undeterminedPlanUrl = undeterminedPlanUrl.replace("{workload}", $("#change-workload").text().toUpperCase());
		$.post(undeterminedPlanUrl, function(data) { });
		moveToNextIssue();
	});
	
	$("#ud-feedback").click(function() {
		var undeterminedFeedbackUrl = "/undetermined/{issueId}/feedback";
		undeterminedFeedbackUrl = undeterminedFeedbackUrl.replace("{issueId}", $("aside section.active").attr("issue-id"));
		$.post(undeterminedFeedbackUrl, function(data) { });
		moveToNextIssue();
	});
	
	$("#ud-critical").click(function() {
		var undeterminedCriticalUrl = "/undetermined/{issueId}/critical";
		undeterminedCriticalUrl = undeterminedCriticalUrl.replace("{issueId}", $("aside section.active").attr("issue-id"));
		$.post(undeterminedCriticalUrl, function(data) {
			$("aside section.active i").attr("class", $("aside section.active i").attr("class").replace("undetermined-issue", "critical-issue"));
			$("#change-progress").text("In progress");
			$("#change-impact").text("High impact");
			$("#change-urgency").text("High priority");
			determineUndecided();
		});
	});
});

function moveToNextIssue() {
	var nextIssue = $("aside section.active").next();
	$("aside section.active").remove();
	nextIssue.click();
}

