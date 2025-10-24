package com.goodee.corpdesk.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeSecurityDTO implements UserDetails {

	private String username;
	private String password;
	private String name;

	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;

	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private String modifiedBy;
	private Boolean useYn;

	private Integer roleId;
	private String roleName;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (roleName != null) {
			authorities.add(new SimpleGrantedAuthority(this.roleName));
		}

		return authorities;
	}
}
