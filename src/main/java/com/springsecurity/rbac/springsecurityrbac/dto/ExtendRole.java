package com.springsecurity.rbac.springsecurityrbac.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class ExtendRole {
    private String username;
    private Collection<PagesPrivilegesDto> pagesPrivilegesDtos;
}
