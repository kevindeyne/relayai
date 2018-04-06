package com.kevindeyne.tasker.controller

import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ForwardController {
	
	companion object {
		const val REDIR = "redirect:"
	}
	
	@GetMapping("/", "/loginSuccess")
	fun forwardLogic() : String {
		if (SecurityHolder.hasRole(Role.DEVELOPER) || SecurityHolder.hasRole(Role.TESTER)) {
			if(SecurityHolder.isReport()) {
				return redir(StandupController.STANDUP_REPORT)
			} else {
				return redir(TaskboardController.TASKBOARD_GET)
			}
		} else if (SecurityHolder.hasRole(Role.TEAM_LEADER)) {
			return redir(TaskboardController.TASKBOARD_GET)
		} else if (SecurityHolder.hasRole(Role.SHAREHOLDER)) {
			return redir(OverviewController.OVERVIEW)
		}
		
		return redir(LandingController._GET)
	}
	
	private fun redir(redirectTo : String) : String {
		return "${ForwardController.REDIR}$redirectTo"
	}
	
}