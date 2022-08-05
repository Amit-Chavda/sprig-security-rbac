package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.entity.Privilege;
import com.springsecurity.rbac.springsecurityrbac.repository.PrivilegeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PrivilegeService {
    private Logger logger = LoggerFactory.getLogger(PrivilegeService.class);

    private PrivilegeRepository privilegeRepository;

    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    public Privilege findByName(String name) {
        Optional<Privilege> privilege = privilegeRepository.findByName(name);
        if (privilege.isEmpty()) {
            logger.info("Privilege with name {} not found!", name);
            return null;
        }
        return privilege.get();
    }


    public Privilege save(Privilege privilege) {
        return privilegeRepository.save(privilege);
    }
}
