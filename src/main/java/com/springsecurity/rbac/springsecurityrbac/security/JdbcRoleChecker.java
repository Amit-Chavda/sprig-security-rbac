package com.springsecurity.rbac.springsecurityrbac.security;

import com.springsecurity.rbac.springsecurityrbac.entity.contsants.PRIVILEGE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

@Component(value = "roleChecker")
public class JdbcRoleChecker implements RoleChecker {

    private Logger logger = LoggerFactory.getLogger(JdbcRoleChecker.class);
   // private Supplier<Set<AntPathRequestMatcher>> supplier;

    @Override
    public boolean check(Authentication authentication, HttpServletRequest request) {
        //todo: not null check
        if (authentication == null || !authentication.isAuthenticated() || request.getParameter("pageCode") == null) {
            logger.error("Unauthorized user or invalid url access!");
            return false;
        }

        String pageCode = request.getParameter("pageCode").toUpperCase();

        if (hasAccessToPage(authentication.getAuthorities(), pageCode)) {
            if (hasAuthorityToAction(authentication.getAuthorities(), request.getMethod())) {
                logger.warn("User {} performed {} on {} ", authentication.getName(), request.getMethod(), pageCode);
                return true;
            }
            logger.warn("User with id {} tried to perform unauthorized activity {} on page {}", authentication.getName(), request.getMethod(), pageCode);
        }
        logger.warn("User with id {} tried to access unauthorized page {}", authentication.getName(), pageCode);
        return false;
    }

    private boolean hasAuthorityToAction(Collection<? extends GrantedAuthority> authorities, String method) {
        String privilege = null;
        switch (method.toUpperCase()) {
            case "GET":
                privilege = PRIVILEGE.READ;
                break;
            case "PUT":
                privilege = PRIVILEGE.UPDATE;
                break;
            case "POST":
                privilege = PRIVILEGE.CREATE;
                break;
            case "DELETE":
                privilege = PRIVILEGE.DELETE;
                break;
        }

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().split("\\.")[1].equals(privilege)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //Assert.notNull(supplier.get(), "function must not be null");
    }

    boolean hasAccessToPage(Collection<? extends GrantedAuthority> authorities, String pageCode) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().split("\\.")[0].equals(pageCode)) {
                return true;
            }
        }
        return false;
    }
}
