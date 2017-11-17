package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ForwardController {
	
	companion object {
		const val REDIR = "redirect:"
	}
	
	@GetMapping("/")
	fun forwardLogic() : String {
		return redir(TaskboardController.TASKBOARD_GET);
	}
	
	private fun redir(redirectTo : String) : String {
		return "${ForwardController.REDIR}${redirectTo}";
	}
	
}