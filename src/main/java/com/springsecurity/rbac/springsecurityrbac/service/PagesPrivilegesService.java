package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.entity.security.PagesPrivileges;
import com.springsecurity.rbac.springsecurityrbac.repository.PagesPrivilegesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagesPrivilegesService {
    private PagesPrivilegesRepository pagesPrivilegesRepository;

    public PagesPrivilegesService(PagesPrivilegesRepository pagesPrivilegesRepository) {
        this.pagesPrivilegesRepository = pagesPrivilegesRepository;
    }

    public Optional<PagesPrivileges> alreadyExists(PagesPrivileges pagesPrivileges) {
        return pagesPrivilegesRepository.alreadyExists(pagesPrivileges.getPrivilege().getId(), pagesPrivileges.getPage().getId());

    }
}
