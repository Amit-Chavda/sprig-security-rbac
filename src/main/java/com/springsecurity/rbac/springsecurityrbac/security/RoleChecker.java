package com.springsecurity.rbac.springsecurityrbac.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface RoleChecker extends InitializingBean {
    boolean check(Authentication authentication);
}
