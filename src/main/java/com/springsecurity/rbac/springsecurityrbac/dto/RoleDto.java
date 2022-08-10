package com.springsecurity.rbac.springsecurityrbac.dto;

import com.springsecurity.rbac.springsecurityrbac.entity.security.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private String name;
    private Collection<PageDto> pages;
}
