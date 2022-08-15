package com.springsecurity.rbac.springsecurityrbac.repository;

import com.springsecurity.rbac.springsecurityrbac.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    boolean existsByName(String name);
}
