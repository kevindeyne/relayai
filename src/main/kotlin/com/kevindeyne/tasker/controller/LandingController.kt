package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LandingController {

	companion object {
		const val _GET = "/welcome"
	}

	@GetMapping(_GET)
	fun getLanding() : String {
		return "landing"
	}
	
}