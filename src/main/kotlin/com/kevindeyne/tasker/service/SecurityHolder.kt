package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.domain.InProgressIssue
import com.kevindeyne.tasker.domain.Role
import com.kevindeyne.tasker.domain.UserPrincipal
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

object SecurityHolder {
	
	fun getUserId() : Long {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {
			//println("user id: ${principal.userId}")
			return principal.userId
		}
		throw RuntimeException("No userId at this point")
	}
	
	fun getSprintId() : Long? {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {
			//println("sprint id: ${principal.sprintId}")
			return principal.sprintId
		}
		return null;
	}
	
	fun getProjectId() : Long {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {
			//println("project id: ${principal.projectId}")
			return principal.projectId
		}
		throw RuntimeException("No projectId at this point")
	}
	
	fun getRoles() : List<Role> {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal) {		    
			return principal.roles
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
	
	fun isReport() : Boolean {
		val principal: UserPrincipal? = getUserPrincipal()
		if (null != principal && principal.sprintReport) {			
			return true
		}
		return false
	}
	
	fun changeProject(projectId : Long, sprintId : Long) {
		val authentication : Authentication = SecurityContextHolder.getContext().authentication
		if (!(authentication is AnonymousAuthenticationToken)) {		    
			val principal = authentication.principal as UserPrincipal
			println("Switching projectid: ${principal.projectId}, sprintid: ${principal.sprintId} to $projectId, $sprintId")
			principal.sprintId = sprintId
			principal.projectId = projectId
		}
	}
}