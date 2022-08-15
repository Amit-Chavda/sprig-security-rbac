package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePagesPrivileges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Role.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @ManyToOne(targetEntity = PagesPrivileges.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "pages_privileges_id", referencedColumnName = "id")
    private PagesPrivileges pagesPrivileges;
}
