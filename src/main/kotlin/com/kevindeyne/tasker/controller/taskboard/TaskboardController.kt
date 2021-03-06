package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.domain.*
import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class TaskboardController(var issueRepository : IssueRepository) {
	
	companion object {
		const val TASKBOARD_GET = "/tasks"
		const val TASKBOARD_GET_CREATE_NEW_ISSUE = "/tasks/create"
		const val TASKBOARD_GET_SPECIFIC_ISSUE = "/tasks/{issueId}"
	}
	
	@GetMapping(TASKBOARD_GET)
	fun getTaskboard(model : Model) : String {
		genericTaskboardBuildup(model)

		val firstIssue : IssueResponse = issueRepository.findHighestPrioForUser()
		specificsTaskboardBuildup(model, firstIssue)
		
		return "taskboard"
	}
		
	@GetMapping(TASKBOARD_GET_CREATE_NEW_ISSUE)
	fun getTaskboardWithCreateNewIssuePage(model : Model) : String {
		genericTaskboardBuildup(model)

		val firstIssue : IssueResponse = issueRepository.findHighestPrioForUser()
		specificsTaskboardBuildup(model, firstIssue)

		model.addAttribute("showCreatePage", true)
		return "taskboard"
	}
	
	@GetMapping(TASKBOARD_GET_SPECIFIC_ISSUE)
	fun getTaskboardWithSpecificIssuePage(model : Model, @PathVariable issueId : String) : String {
		genericTaskboardBuildup(model)
		var reponse : IssueResponse? = issueRepository.findById(issueId.toLong())
		if(reponse == null){ return "redirect:/tasks" }
		specificsTaskboardBuildup(model, reponse)
		return "taskboard"
	}
	
	///
	
	fun genericTaskboardBuildup(model : Model) {
		val issueList = issueRepository.findAllActiveForUserInCurrentSprint()

		model.addAttribute("issueList", issueList)
		model.addAttribute("showCreatePage", issueList.isEmpty())
		model.addAttribute("urlPostIssue", IssueController.ISSUE_DETAIL)

		model.addAttribute("progressStates", Progress.values())
		model.addAttribute("impactStates", Impact.values())
		model.addAttribute("urgencyStates", Urgency.values())
		model.addAttribute("workloadStates", Workload.values())

		model.addAttribute("maxid", determineMaxId(issueList))

		model.addAttribute("activePage", "tasks")
	}
	
	fun determineMaxId(list : List<IssueListing>) : Long = if(list.isEmpty()) 0L else list.stream().mapToLong{ i -> i.id }.max().asLong
	
	fun specificsTaskboardBuildup(model : Model, issue : IssueResponse) {
		model.addAttribute("currentIssueId", issue.id)
		model.addAttribute("currentProjectTitle", issue.project)
		model.addAttribute("currentIssueTitle", issue.title)
		model.addAttribute("currentIssueDescription", issue.descr)
		model.addAttribute("currentIssueProgress", issue.status)
		model.addAttribute("currentIssueImpact", issue.impact)
		model.addAttribute("currentIssueUrgency", issue.urgency)
		model.addAttribute("currentIssueFixVersion1", "[Milestone Alpha] 3.2.14")
		model.addAttribute("currentIssueFixVersion2", "[Milestone Beta] 4.1.1")
		model.addAttribute("currentIssueCreator", issue.creator)
		model.addAttribute("currentIssueCreateDate", issue.createDate)
		model.addAttribute("currentIssueSLAStatus", issue.slaStatus)
		
		model.addAttribute("comments", issue.comments)
		model.addAttribute("versions", issue.versions)
		
		//<span th:each="version, iterStat : ${versions}" data-status="Current fix version" class="changeable" th:id="${'change-version-' + iterStat.index}" th:text="${urgencyState.text}">No current version</span>
		
		model.addAttribute("showCreatePage", false)
	}
	
}