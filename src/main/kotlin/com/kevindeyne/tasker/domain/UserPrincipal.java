package com.kevindeyne.tasker.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -7668331954068127632L;

	private Long userId;

	private String userName;

	private String password;

	private Long projectId;

	private Long sprintId;

	private List<Role> roles;

	private List<InProgressIssue> trackingIssues;

	public UserPrincipal(Long userId, String userName, String password, Long projectId, Long sprintId, List<Role> roles, List<InProgressIssue> issues) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.setTrackingIssues(issues);
		this.setRoles(roles);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getSprintId() {
		return sprintId;
	}

	public void setSprintId(Long sprintId) {
		this.sprintId = sprintId;
	}

	public List<InProgressIssue> getTrackingIssues() {
		return trackingIssues;
	}

	public void setTrackingIssues(List<InProgressIssue> trackingIssues) {
		this.trackingIssues = trackingIssues;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}