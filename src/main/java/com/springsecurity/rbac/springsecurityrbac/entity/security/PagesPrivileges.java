package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
public class PagesPrivileges {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Page.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private Page page;


    @ManyToOne(targetEntity = Privilege.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    private Privilege privilege;


    @ManyToMany(mappedBy = "pagesPrivileges", fetch = FetchType.EAGER)
    private Collection<Role> roles;

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
