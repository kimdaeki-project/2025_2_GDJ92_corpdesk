package com.goodee.corpdesk.employee;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleInit implements InitializingBean {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {
//		Role admin = new Role();
//		admin.setRoleId(1);
//		admin.setRoleName("ROLE_ADMIN");
//		roleRepository.save(admin);
//
//		Role user = new Role();
//		user.setRoleId(2);
//		user.setRoleName("ROLE_USER");
//		roleRepository.save(user);
	}
}
