package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.domain.IssueListing
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
		
		val issueList : List<IssueListing> = model.asMap().get("issueList") as List<IssueListing>
		val firstIssue = issueList.first()
		
		specificsTaskboardBuildup(model, firstIssue)
		
		return "taskboard"
	}
		
	@GetMapping(TASKBOARD_GET_CREATE_NEW_ISSUE)
	fun getTaskboardWithCreateNewIssuePage(model : Model) : String {
		genericTaskboardBuildup(model)
		model.addAttribute("showCreatePage", true)
		return "taskboard"
	}
	
	@GetMapping(TASKBOARD_GET_SPECIFIC_ISSUE)
	fun getTaskboardWithSpecificIssuePage(model : Model, @PathVariable issueId : String) : String {
		genericTaskboardBuildup(model)
		specificsTaskboardBuildup(model, issueRepository.findById(issueId.toLong()))
		return "taskboard"
	}
	
	
	fun getCurrentUserId() = ""
	
	fun genericTaskboardBuildup(model : Model) {
		val issueList = issueRepository.findAllForUser(getCurrentUserId())
		model.addAttribute("issueList", issueList);
		model.addAttribute("urlPostIssue", IssueController.ISSUE_POST)
	}
	
	fun specificsTaskboardBuildup(model : Model, issue : IssueListing) {
		model.addAttribute("currentIssueId", issue.id)
		model.addAttribute("currentIssueTitle", issue.title)
		model.addAttribute("currentIssueDescription", issue.longDescr)
		model.addAttribute("showCreatePage", false)
	}
	
}