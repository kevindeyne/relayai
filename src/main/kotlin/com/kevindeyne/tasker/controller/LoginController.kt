package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@Controller
class LoginController() {
		
	companion object {
		const val LOGIN_GET = "/login"
		const val LOGOUT_GET = "/logout"
	}
	
	@GetMapping(LOGIN_GET)
	fun getTaskboard(principal : Principal?) : String = if (principal == null) "login" else "redirect:/" 
	
}