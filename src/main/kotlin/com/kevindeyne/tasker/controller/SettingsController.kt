package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SettingsController() {
	
	companion object {
		const val SETTINGS_GET = "/settings"
	}
	
	@GetMapping(SETTINGS_GET)
	fun getSearch(model : Model) : String {
		model.addAttribute("activePage", "settings");
		return "settings"
	}	
}