package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class RolePagesPrivileges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Role.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @ManyToOne(targetEntity = PagesPrivileges.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pages_privileges_id", referencedColumnName = "id")
    private PagesPrivileges pagesPrivileges;
}
