package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.controller.form.IssueResponse
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.SprintRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class StandupController(var issueRepository : IssueRepository, var sprintRepository : SprintRepository) {
	
	companion object {
		const val STANDUP_GET = "/daily"
		const val STANDUP_GET_SPECIFIC = "/daily/{sprintid}/{day}"
	}
	
	@GetMapping(STANDUP_GET)
	fun getDailyOverviewOfYesterday(model : Model) : String {
		
		getDailyOverviewOfSpecificSprint(model, sprintRepository.findCurrentSprintId())
		
		//TODO test data
		val issueList = issueRepository.findAllForUser()
				
		model.addAttribute("issueList1", issueList.subList(0, 5));
		model.addAttribute("issueList2", issueList.subList(6, 16));
		model.addAttribute("issueList3", issueList.subList(17, 38));
		
		return "standup"
	}
		
	///

	fun getDailyOverviewOfSpecificSprint(model : Model, sprintId : Long) { //TODO day??
		model.addAttribute("yesterdayIssues", issueRepository.findStandupIssuesForSprint(sprintId))
		
		model.addAttribute("activePage", "sprint");
	}
	
}