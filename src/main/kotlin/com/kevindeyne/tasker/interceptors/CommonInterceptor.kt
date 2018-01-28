package com.kevindeyne.tasker.interceptors

import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CommonInterceptor(val issueRepository : IssueRepository) : HandlerInterceptorAdapter() {
	
	val blacklist = listOf("pull", "login", "logout", "error",
			"/stylesheets/", "/js/", "/icons/", "/fonts/")
	
	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?) : Boolean {		
		if(isValidHandle(request)){
			val reqUrl = request.getRequestURL().toString()
			if(SecurityHolder.getProjectId() == -1L && !reqUrl.contains("/project/new")){
				response.sendRedirect("/project/new");							
				return false
			}
		}

		return true
	}

	override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, model: ModelAndView?) {
		if(model != null && isValidHandle(request)){		
			if (SecurityHolder.hasRole(Role.DEVELOPER) || SecurityHolder.hasRole(Role.TESTER)) {					
				model.addObject("inProgressIssueList", issueRepository.findAllInProgress())
			} 
		}				
	}
	
	fun isValidHandle(request: HttpServletRequest) : Boolean {
		val context = SecurityContextHolder.getContext()
		if(context != null && context.authentication != null){
			val authentication : Authentication = context.getAuthentication()
			if (!(authentication is AnonymousAuthenticationToken)) {				
				val reqUrl = request.getRequestURL().toString()
				for(listitem in blacklist){ if(reqUrl.contains(listitem)) { return false; } }				
				return true
			}
		}
		return false
	}
}