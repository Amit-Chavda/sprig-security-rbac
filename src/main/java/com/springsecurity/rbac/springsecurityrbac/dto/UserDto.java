package com.springsecurity.rbac.springsecurityrbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean enabled;
    private boolean specialPrivileges=false;
    private Collection<RoleDto> roles;

}

