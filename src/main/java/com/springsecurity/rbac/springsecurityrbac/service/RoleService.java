package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.entity.Role;
import com.springsecurity.rbac.springsecurityrbac.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private Logger logger = LoggerFactory.getLogger(RoleService.class);

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            logger.info("Role with name {} not found!", name);
            return null;
        }
        return role.get();
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
