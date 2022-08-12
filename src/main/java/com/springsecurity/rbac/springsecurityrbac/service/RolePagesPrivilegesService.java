package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.entity.security.RolePagesPrivileges;
import com.springsecurity.rbac.springsecurityrbac.repository.RolePagesPrivilegesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolePagesPrivilegesService {

    private RolePagesPrivilegesRepository rolePagesPrivilegesRepository;

    public RolePagesPrivilegesService(RolePagesPrivilegesRepository rolePagesPrivilegesRepository) {
        this.rolePagesPrivilegesRepository = rolePagesPrivilegesRepository;
    }

    public Optional<RolePagesPrivileges> alreadyExists(RolePagesPrivileges rolePagesPrivileges) {
        return rolePagesPrivilegesRepository.alreadyExists(rolePagesPrivileges.getRole().getId(), rolePagesPrivileges.getPagesPrivileges().getId());

    }

    public RolePagesPrivileges save(RolePagesPrivileges rolePagesPrivileges) {
        Optional<RolePagesPrivileges> pagesPrivilegesOptional = alreadyExists(rolePagesPrivileges);
        return pagesPrivilegesOptional.orElseGet(() -> rolePagesPrivilegesRepository.save(rolePagesPrivileges));
    }
}
