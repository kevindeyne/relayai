package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.domain.InProgressIssue
import com.kevindeyne.tasker.domain.Role
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
	
	fun getRoles() : List<Role> {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {		    
			return principal.roles;
		}
		return listOf();
	}
	
	fun hasRole(role : Role) : Boolean {
		getRoles().forEach{ r -> return r.equals(role) }
		return false
	}
	
	fun getUserPrincipal() : UserPrincipal? {
		val authentication : Authentication = SecurityContextHolder.getContext().getAuthentication()
		if (!(authentication is AnonymousAuthenticationToken)) {		    
			return authentication.principal as UserPrincipal
		}
		return null
	}
	
	fun addTrackingIssue(issueId : Long, title : String) {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {
			val issueListing = InProgressIssue(issueId, title)
			principal.trackingIssues.add(issueListing)
		}
	}	
}