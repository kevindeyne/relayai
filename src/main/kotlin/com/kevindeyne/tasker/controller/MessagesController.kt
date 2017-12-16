package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MessagesController() {
	
	companion object {
		const val MESSAGES_GET = "/messages"
	}
	
	@GetMapping(MESSAGES_GET)
	fun getSearch(model : Model) : String {
		model.addAttribute("activePage", "messages");
		return "messages"
	}	
}