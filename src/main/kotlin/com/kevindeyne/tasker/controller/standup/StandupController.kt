package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.domain.IssueListing
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.repositories.SprintRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class StandupController(val issueRepository : IssueRepository, val sprintRepository : SprintRepository) {
	
	companion object {
		const val STANDUP_REPORT = "/report"
		const val STANDUP_GET = "/daily"
		const val STANDUP_GET_SPECIFIC = "/daily/{sprintid}/{day}"
	}
	
	@GetMapping(STANDUP_REPORT)
	fun getReportOffinishedSprint(model : Model) : String {
		val sprintId = SecurityHolder.getSprintId();
		if(sprintId == null || !SecurityHolder.isReport()){ return "redirect:/" }
		
		model.addAttribute("report", true);
		getDailyOverviewOfSpecificSprint(model, sprintId)

		//TODO test data
		val issueList = issueRepository.findAllActiveForUserInCurrentSprint()				
		model.addAttribute("issueList1", issueList.subList(0, 2));
		model.addAttribute("issueList2", issueList.subList(2, 4));
		model.addAttribute("issueList3", issueList.subList(4, 5));
		//end testData
		
		return "standup"
	}
	
	@GetMapping(STANDUP_GET)
	fun getDailyOverviewOfYesterday(model : Model) : String {
		val sprintId = SecurityHolder.getSprintId();		
		if(sprintId == null){ return "redirect:/" }
	
		model.addAttribute("report", false);
		getDailyOverviewOfSpecificSprint(model, sprintId)
				
		//test data
		val issueList = issueRepository.findAllActiveForUserInCurrentSprint()
		if(!issueList.isEmpty()){
			model.addAttribute("issueList1", exampleList(issueList, 0, 2));
			model.addAttribute("issueList2", exampleList(issueList, 2, 4));
			model.addAttribute("issueList3", exampleList(issueList, 4, 5));	
		}		
		//end testData		
		return "standup"
	}
	
	fun exampleList(list : List<IssueListing>, start : Int, end : Int) : List<IssueListing> {
		try{
			return list.subList(start, end)
		}catch(e : IndexOutOfBoundsException){
			return listOf()
		}
	}

	fun getDailyOverviewOfSpecificSprint(model : Model, sprintId : Long) { //TODO day??
		model.addAttribute("yesterdayIssues", issueRepository.findStandupIssuesForSprint(sprintId))
		
		model.addAttribute("activePage", "sprint");
	}
	
}