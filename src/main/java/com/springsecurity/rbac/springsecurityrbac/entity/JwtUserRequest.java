package com.springsecurity.rbac.springsecurityrbac.entity;

import lombok.Data;

@Data
public class JwtUserRequest {
    private String email;
    private String password;
}
