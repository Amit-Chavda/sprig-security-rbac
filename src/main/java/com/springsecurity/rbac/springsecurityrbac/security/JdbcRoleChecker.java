package com.springsecurity.rbac.springsecurityrbac.security;

import com.springsecurity.rbac.springsecurityrbac.util.Console;
import com.sun.istack.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.function.Supplier;

public class JdbcRoleChecker implements RoleChecker {

    private Supplier<Set<AntPathRequestMatcher>> supplier;

    @Override
    public boolean check(Authentication authentication, HttpServletRequest request) {
       //todo: not null check
        if (!authentication.isAuthenticated()) {
            return false;
        }
        supplier.get().stream().forEach(antPathRequestMatcher -> {
            Console.println(antPathRequestMatcher.getPattern(), RoleChecker.class);
        });

        //filter(matcher -> matcher.matches(request));

        Console.println(authentication.getAuthorities().toString(), JdbcRoleChecker.class);
        Console.println(authentication.getName(), JdbcRoleChecker.class);
        Console.println(authentication.getPrincipal().toString(), JdbcRoleChecker.class);
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(supplier.get(), "function must not be null");
    }
}
