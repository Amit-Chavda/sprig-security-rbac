package com.springsecurity.rbac.springsecurityrbac.repository;

import com.springsecurity.rbac.springsecurityrbac.entity.security.RolePagesPrivileges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePagesPrivilegesRepository extends JpaRepository<RolePagesPrivileges, Long> {
    @Query(
            value = "SELECT rp from RolePagesPrivileges as rp "
                    + "JOIN FETCH rp.role rl "
                    + "JOIN FETCH rp.pagesPrivileges pp "
                    + "where rl.id = ?1 "
                    + "and pp.id = ?2 "
    )
    Optional<RolePagesPrivileges> alreadyExists(Long id, long id1);
}
