package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private Collection<PagesPrivileges> pagesPrivileges;

    public Page(String name) {
        this.name = name;
    }

    public void setPagesPrivileges(Collection<PagesPrivileges> pagesPrivileges) {
        for (PagesPrivileges privileges : pagesPrivileges) {
            privileges.setPage(this);
        }
        this.pagesPrivileges = pagesPrivileges;
    }
}
