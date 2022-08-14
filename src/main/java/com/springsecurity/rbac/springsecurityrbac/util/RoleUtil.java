package com.springsecurity.rbac.springsecurityrbac.util;

import com.springsecurity.rbac.springsecurityrbac.dto.PageDto;
import com.springsecurity.rbac.springsecurityrbac.dto.PrivilegeDto;
import com.springsecurity.rbac.springsecurityrbac.dto.RoleDto;
import com.springsecurity.rbac.springsecurityrbac.entity.security.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleUtil {

    public static RoleDto toRoleDto(Role role) {

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
    }

    public static List<RoleDto> toRoleDtos(List<Role> all) {
        return all.stream().map(role -> toRoleDto(role)).toList();
    }

    /*public static Role toRole(RoleDto roleDto) {


        List<PagesPrivileges> pagesPrivilegesList = roleDto.getPagePrivilegeMap().entrySet()
                .stream()
                .map(pageDtoListEntry -> {

                    Page page = new Page(pageDtoListEntry.getKey().getName());

                    List<Privilege> privileges = pageDtoListEntry.getValue().stream()
                            .map(privilegeDto ->
                                    new Privilege(privilegeDto.getName())
                            ).toList();

                    List<PagesPrivileges> pagesPrivilegesList1 = new ArrayList<>();

                    for (Privilege privilege : privileges) {
                        PagesPrivileges pagesPrivileges = new PagesPrivileges();
                        pagesPrivileges.setPage(page);
                        pagesPrivileges.setPrivilege(privilege);
                        pagesPrivilegesList1.add(pagesPrivileges);
                    }

                    return pagesPrivilegesList1;
                })
                .toList()
                .stream()
                .flatMap(List::stream)
                .toList();

        pagesPrivilegesList.stream()
                .map(pagesPrivileges -> {
                    Role role = new Role();
                    role.setName(roleDto.getName());
                    RolePagesPrivileges rolePagesPrivileges = new RolePagesPrivileges();
                    rolePagesPrivileges.setPagesPrivileges(pagesPrivileges);
                    rolePagesPrivileges.setRole(role);
                    return role;
                }).collect(Collectors.toSet());


    }*/


}
