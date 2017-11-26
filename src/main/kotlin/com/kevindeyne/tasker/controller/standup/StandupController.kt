package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.SprintRepository
import com.kevindeyne.tasker.service.SecurityHolder
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
		val sprintId = SecurityHolder.getSprintId();		
		if(sprintId == null){ return "redirect:/" }
	
		getDailyOverviewOfSpecificSprint(model, sprintId)
				
		//TODO test data
		val issueList = issueRepository.findAllForUser()				
		model.addAttribute("issueList1", issueList.subList(0, 5));
		model.addAttribute("issueList2", issueList.subList(6, 16));
		model.addAttribute("issueList3", issueList.subList(17, 38));
		//end testData
		
		return "standup"
	}

	fun getDailyOverviewOfSpecificSprint(model : Model, sprintId : Long) { //TODO day??
		model.addAttribute("yesterdayIssues", issueRepository.findStandupIssuesForSprint(sprintId))
		
		model.addAttribute("activePage", "sprint");
	}
	
}