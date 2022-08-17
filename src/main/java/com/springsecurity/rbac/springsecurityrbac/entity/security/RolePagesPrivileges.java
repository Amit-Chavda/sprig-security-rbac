package com.springsecurity.rbac.springsecurityrbac.entity.security;

import com.springsecurity.rbac.springsecurityrbac.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

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

    @ManyToMany(mappedBy = "rolePagesPrivileges", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<User> users;
}
