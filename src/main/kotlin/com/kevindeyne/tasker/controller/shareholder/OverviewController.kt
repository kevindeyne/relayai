package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal
import javax.servlet.http.HttpServletRequest

@Controller
class OverviewController() {

	companion object {
		const val OVERVIEW = "/overview"
	}
	
	@GetMapping(OVERVIEW)
	fun getOverview() : String = "overview"
		
}