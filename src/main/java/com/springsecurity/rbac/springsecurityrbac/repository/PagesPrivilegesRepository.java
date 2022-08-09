package com.springsecurity.rbac.springsecurityrbac.repository;

import com.springsecurity.rbac.springsecurityrbac.entity.security.PagesPrivileges;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagesPrivilegesRepository extends JpaRepository<PagesPrivileges,Long> {
}
