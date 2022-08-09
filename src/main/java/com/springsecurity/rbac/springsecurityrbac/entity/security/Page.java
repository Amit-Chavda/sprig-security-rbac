package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    /* @ManyToMany(fetch = FetchType.EAGER)
     @JoinTable(
             name = "pages_privileges",
             joinColumns = @JoinColumn(
                     name = "page_id", referencedColumnName = "id"),
             inverseJoinColumns = @JoinColumn(
                     name = "privilege_id", referencedColumnName = "id"))
     private Collection<Privilege> privileges;

     @ManyToMany(mappedBy = "pages")
     private Collection<Role> roles;
 */
    // @JsonIgnore
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
