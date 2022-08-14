package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssignRole {
    private String username;
    private List<String> roleNames;
}
