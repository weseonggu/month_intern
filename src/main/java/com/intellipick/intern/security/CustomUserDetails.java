package com.intellipick.intern.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	private final Long id;
	private ForContext context;
	public CustomUserDetails(ForContext forContext) {
		this.context = forContext;
		this.id = context.getId();
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<GrantedAuthority> authorities = context.getAuthoritiesSet().stream()
				.map(role -> new SimpleGrantedAuthority(role.getValue()))
				.collect(Collectors.toSet());

		return authorities;

	}
	public ForContext getForContext() {
		return context;
	}
	public Long getId() {
		return id;
	}
	@Override
	public String getPassword() {
		return "";
	}
	@Override
	public String getUsername() {
		return context.getUsername();
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
}
