package com.springsecurity.rbac.springsecurityrbac.dto;

import com.springsecurity.rbac.springsecurityrbac.entity.security.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private String name;
    private Map<PageDto, List<PrivilegeDto>> pagePrivilegeMap;

}
