package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SearchController() {
	
	companion object {
		const val SEARCH_GET = "/search"
	}
	
	@GetMapping(SEARCH_GET)
	fun getSearch(model : Model) : String {
		model.addAttribute("activePage", "search");
		return "search"
	}	
}