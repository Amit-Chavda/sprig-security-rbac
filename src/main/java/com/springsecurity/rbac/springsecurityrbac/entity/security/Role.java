package com.springsecurity.rbac.springsecurityrbac.entity.security;

import com.springsecurity.rbac.springsecurityrbac.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "pages_privileges_roles",
            joinColumns = @JoinColumn(
                    name = "pages_privileges_id",
                    referencedColumnName = "id"
            ), inverseJoinColumns = @JoinColumn(
            name = "role_id",
            referencedColumnName = "id")
    )
    private Collection<PagesPrivileges> pagesPrivileges;

    public Role(String name) {
        this.name = name;
    }

    public void setPagesPrivileges(Collection<PagesPrivileges> pagesPrivileges) {
        for (PagesPrivileges privileges : pagesPrivileges) {
            privileges.setRoles(List.of(this));
        }

        this.pagesPrivileges = pagesPrivileges;
    }
}
