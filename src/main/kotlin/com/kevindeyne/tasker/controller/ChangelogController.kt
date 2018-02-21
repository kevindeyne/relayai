package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.domain.BranchListing
import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MessagesController(val issueRepository : IssueRepository) {
	
	companion object {
		const val CHANGELOG_GET = "/changelog"
	}
	
	@GetMapping(CHANGELOG_GET)
	fun getChangelog(model : Model) : String {
		model.addAttribute("activePage", "changelog")
		
		val issueList = issueRepository.findAllActiveForUserInCurrentSprint()
		model.addAttribute("activeChangelog", issueList)
		model.addAttribute("branches", listOf(BranchListing()))
		model.addAttribute("currentBranch", BranchListing())
				
		return "changelog"
	}	
}