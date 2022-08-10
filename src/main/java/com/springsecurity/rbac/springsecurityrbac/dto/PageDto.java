package com.springsecurity.rbac.springsecurityrbac.dto;

import com.springsecurity.rbac.springsecurityrbac.entity.security.Privilege;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    private String name;
    private Collection<PrivilegeDto> privileges;
}
