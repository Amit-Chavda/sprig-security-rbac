package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(mappedBy = "page", cascade = {CascadeType.MERGE})
    private Collection<PagesPrivileges> pagesPrivileges;

    public Page(String name) {
        this.name = name;
    }


    private transient Collection<Privilege> privileges;

    @Transient
    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Transient
    public Collection<Privilege> getPrivileges() {
        return privileges;
    }
}
