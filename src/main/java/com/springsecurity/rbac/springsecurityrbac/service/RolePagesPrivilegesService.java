package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.entity.security.RolePagesPrivileges;
import com.springsecurity.rbac.springsecurityrbac.repository.RolePagesPrivilegesRepository;
import org.springframework.stereotype.Service;

@Service
public class RolePagesPrivilegesService {

    private RolePagesPrivilegesRepository rolePagesPrivilegesRepository;

    public RolePagesPrivilegesService(RolePagesPrivilegesRepository rolePagesPrivilegesRepository) {
        this.rolePagesPrivilegesRepository = rolePagesPrivilegesRepository;
    }

    public RolePagesPrivileges save(RolePagesPrivileges rolePagesPrivileges) {
        return rolePagesPrivilegesRepository.save(rolePagesPrivileges);
    }
}
