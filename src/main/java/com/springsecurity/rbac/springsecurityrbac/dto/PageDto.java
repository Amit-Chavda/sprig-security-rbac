package com.springsecurity.rbac.springsecurityrbac.dto;

import com.springsecurity.rbac.springsecurityrbac.entity.security.Privilege;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    private String name;
}
