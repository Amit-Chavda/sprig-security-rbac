package com.springsecurity.rbac.springsecurityrbac.util;

import com.springsecurity.rbac.springsecurityrbac.dto.PrivilegeDto;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Privilege;

import java.util.Collection;
import java.util.List;

public class PrivilegeUtil {
    public static Privilege toPrivilege(PrivilegeDto privilegeDto) {
        return new Privilege(privilegeDto.getName());
    }

    public static PrivilegeDto toPrivilegeDto(Privilege privilege) {
        return new PrivilegeDto(privilege.getName());
    }


    public static List<Privilege> toPrivileges(Collection<PrivilegeDto> privilegeDtos) {
        return privilegeDtos.stream().map(privilegeDto -> new Privilege(privilegeDto.getName())).toList();
    }
}
