package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(mappedBy = "privilege", cascade = CascadeType.ALL)
    private Set<PagesPrivileges> pagesPrivileges;

    public Privilege(String name) {
        this.name = name;
    }

    public void setPagesPrivileges(Set<PagesPrivileges> pagesPrivileges) {
        for (PagesPrivileges privileges : pagesPrivileges) {
            privileges.setPrivilege(this);
        }
        this.pagesPrivileges = pagesPrivileges;
    }
}
