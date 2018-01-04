package com.kevindeyne.tasker.interceptors

import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.repositories.IssueRepository
import com.kevindeyne.tasker.service.SecurityHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CommonInterceptor(val issueRepository : IssueRepository) : HandlerInterceptorAdapter() {

	override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, model: ModelAndView) {
		if (SecurityHolder.hasRole(Role.DEVELOPER) || SecurityHolder.hasRole(Role.TESTER)) {
			model.addObject("inProgressIssueList", issueRepository.findAllInProgress())
		}					
	}
}