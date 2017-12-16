package com.kevindeyne.tasker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal
import javax.servlet.http.HttpServletRequest

@Controller
class LoginController() {
		
	companion object {
		const val LOGIN_GET = "/login"
		const val LOGOUT_GET = "/logout"
	}
	
	@GetMapping(LOGIN_GET)
	fun getLoginPage(principal : Principal?) : String = if (principal == null) "login" else "redirect:/"
	
	
	@GetMapping(LOGOUT_GET)
	fun getLogout(principal : Principal?, request : HttpServletRequest) : String {
		if (principal != null){
			request.logout()
		}
		
		return "redirect:/"
	}	
}