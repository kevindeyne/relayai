package com.kevindeyne.tasker.interceptors

import com.kevindeyne.tasker.controller.ForwardController
import org.apache.commons.lang.StringUtils
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HoneypotAuthenticationFilter: UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val honeypot = request?.getParameter("email")
        if(StringUtils.isEmpty(honeypot)){
            response?.sendRedirect(ForwardController.REDIR)
            return super.attemptAuthentication(request, response)
        } else {
            throw AuthenticationServiceException("Authentication failed")
        }
    }
}