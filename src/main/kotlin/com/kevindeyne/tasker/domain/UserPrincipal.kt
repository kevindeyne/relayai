package com.kevindeyne.tasker.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Arrays

data class UserPrincipal constructor(
		val userId: Long,
		val userName: String,
		private val password: String,
		val projectId: Long?,
		val sprintId: Long?,
		val roles: List<Role>,
		val trackingIssues: MutableList<InProgressIssue>
) : UserDetails {

	override fun getAuthorities(): MutableList<GrantedAuthority> {
		return Arrays.asList(SimpleGrantedAuthority("USER"))
	}

	override fun getPassword(): String? = this.password

	override fun getUsername(): String = this.userName

	override fun isAccountNonExpired(): Boolean = true

	override fun isAccountNonLocked(): Boolean = true

	override fun isCredentialsNonExpired(): Boolean = true
	override fun isEnabled(): Boolean = true

}