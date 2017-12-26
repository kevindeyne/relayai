package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal
import javax.servlet.http.HttpServletRequest

@Controller
class DetailController() {
		
	companion object {
		const val ISSUE_DETAIL = "/detail/{id}"
	}
	
	@GetMapping(ISSUE_DETAIL)
	fun getIssueDetails() : String = "detail"
		
}