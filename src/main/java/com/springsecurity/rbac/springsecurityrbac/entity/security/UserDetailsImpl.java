package com.springsecurity.rbac.springsecurityrbac.entity.security;

import com.springsecurity.rbac.springsecurityrbac.dto.PrivilegeDto;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.mapper.RoleMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetailsImpl implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleMapper.toRoleDtos(user.getRoles())
                .stream()
                .map(roleDto -> {
                    Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>(

                            roleDto.getPagePrivilegeMap()
                                    .entrySet()
                                    .stream()
                                    .map(pageDtoListEntry -> {

                                        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

                                        for (PrivilegeDto privilegeDto : pageDtoListEntry.getValue()) {
                                            grantedAuthorities.add(new SimpleGrantedAuthority(
                                                    pageDtoListEntry.getKey().getName() + "." + privilegeDto.getName()
                                            ));
                                        }
                                        return grantedAuthorities;
                                    }).toList()
                                    .stream()
                                    .flatMap(List::stream)
                                    .toList()
                    );
                    simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleDto.getName()));
                    return simpleGrantedAuthorities;
                }).toList()
                .stream()
                .flatMap(Collection::stream)
                .toList();
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