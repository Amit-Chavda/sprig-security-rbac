package com.springsecurity.rbac.springsecurityrbac.util;

import com.springsecurity.rbac.springsecurityrbac.dto.PageDto;
import com.springsecurity.rbac.springsecurityrbac.dto.PrivilegeDto;
import com.springsecurity.rbac.springsecurityrbac.dto.RoleDto;
import com.springsecurity.rbac.springsecurityrbac.dto.UserDto;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.entity.security.PagesPrivileges;
import com.springsecurity.rbac.springsecurityrbac.entity.security.RolePagesPrivileges;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleUserUtil {
    private SimpleUserUtil() {
        throw new IllegalStateException("Authority Utility class");
    }

    public static List<RoleDto> getRoleAndAuthorities(User user) {
        return user.getRoles().stream().map(
                role -> {

                    List<PagesPrivileges> pagesPrivilegesList =
                            role.getRolePagesPrivileges()
                                    .stream()
                                    .map(RolePagesPrivileges::getPagesPrivileges)
                                    .toList();


                    Map<String, List<String>> map = pagesPrivilegesList.stream().collect(
                            Collectors.groupingBy(
                                    pagesPrivileges -> pagesPrivileges.getPage().getName(),
                                    Collectors.mapping(
                                            pagesPrivileges -> pagesPrivileges.getPrivilege().getName(),
                                            Collectors.toList()
                                    )
                            )
                    );

                    Map<PageDto, List<PrivilegeDto>> pageDtoListMap =
                            map.entrySet().stream().collect(
                                    Collectors.toMap(stringListEntry -> new PageDto(stringListEntry.getKey()),
                                            o -> o.getValue().stream().map(PrivilegeDto::new).toList()
                                    ));

                    RoleDto roleDto = new RoleDto();
                    roleDto.setName(role.getName());
                    roleDto.setPagePrivilegeMap(pageDtoListMap);
                    return roleDto;

                }).toList();
    }


    public static List<SimpleGrantedAuthority> getAllGrantedAuthorities(User user) {
        List<RoleDto> roleDtos = getRoleAndAuthorities(user);
        return roleDtos.stream()
                .map(roleDto -> {

                    List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>(
                            roleDto.getPagePrivilegeMap()
                                    .entrySet()
                                    .stream()
                                    .map(pageDtoListEntry -> {

                                        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

                                        for (PrivilegeDto privilegeDto : pageDtoListEntry.getValue()) {
                                            grantedAuthorities.add(new SimpleGrantedAuthority(pageDtoListEntry.getKey().getName() + "." + privilegeDto.getName()));
                                        }
                                        return grantedAuthorities;
                                    }).toList()
                                    .stream()
                                    .flatMap(List::stream)
                                    .toList());
                    simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleDto.getName()));
                    return simpleGrantedAuthorities;
                }).toList()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

}



