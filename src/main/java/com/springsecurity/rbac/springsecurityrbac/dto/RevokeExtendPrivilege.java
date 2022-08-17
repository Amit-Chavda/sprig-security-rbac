package com.springsecurity.rbac.springsecurityrbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class RevokeExtendPrivilege {
    private String username;
    private Map<PageDto, List<PrivilegeDto>> specialPrivilegesMap;
}
