package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.domain.UserPrincipal
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

object SecurityHolder {
	
	fun getUserId() : Long? {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {		    
			return principal.userId;
		}
		return null;
	}
	
	fun getSprintId() : Long? {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {
			return principal.sprintId;
		}
		return null;
	}
	
	fun getProjectId() : Long? {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {		    
			return principal.projectId;
		}
		return null;
	}
	
	fun getUserPrincipal() : UserPrincipal? {
		val authentication : Authentication = SecurityContextHolder.getContext().getAuthentication()
		if (!(authentication is AnonymousAuthenticationToken)) {		    
			return authentication.principal as UserPrincipal
		}
		return null
	}
	
}