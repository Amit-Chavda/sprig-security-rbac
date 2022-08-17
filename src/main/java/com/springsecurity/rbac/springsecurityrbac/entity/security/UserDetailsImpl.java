package com.springsecurity.rbac.springsecurityrbac.entity.security;

import com.springsecurity.rbac.springsecurityrbac.dto.PrivilegeDto;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.mapper.RoleMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (user.isSpecialPrivileges()) {
            grantedAuthorities.addAll(
                    extractSpecialPrivileges().stream().map(
                            pagesPrivileges -> new SimpleGrantedAuthority(
                                    pagesPrivileges.getPage().getName() + "." + pagesPrivileges.getPrivilege().getName()
                            )).toList()
            );
        }

        grantedAuthorities.addAll(
                RoleMapper.toRoleDtos(user.getRoles())
                        .stream()
                        .map(roleDto -> {
                            return roleDto.getPagePrivilegeMap()
                                    .entrySet()
                                    .stream()
                                    .map(pageDtoListEntry -> {

                                        List<SimpleGrantedAuthority> grantedAuthorities1 = new ArrayList<>();

                                        for (PrivilegeDto privilegeDto : pageDtoListEntry.getValue()) {
                                            grantedAuthorities1.add(new SimpleGrantedAuthority(
                                                    pageDtoListEntry.getKey().getName() + "." + privilegeDto.getName()
                                            ));
                                        }
                                        return grantedAuthorities1;
                                    }).toList()
                                    .stream()
                                    .flatMap(List::stream)
                                    .toList();
                            //simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleDto.getName()));
                        }).toList()
                        .stream()
                        .flatMap(List::stream)
                        .toList()
        );
        return grantedAuthorities;

    }

    private Collection<PagesPrivileges> extractSpecialPrivileges() {
        if (user.isSpecialPrivileges()) {
            return user.getRolePagesPrivileges().stream().map(RolePagesPrivileges::getPagesPrivileges).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return user.isEnabled();
    }

}