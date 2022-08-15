package com.springsecurity.rbac.springsecurityrbac.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"privilege_id", "page_id"})
})
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


    @OneToMany(mappedBy = "pagesPrivileges", cascade = CascadeType.ALL)
    private Collection<RolePagesPrivileges> rolePagesPrivileges;

    public PagesPrivileges(Page page, Privilege privilege) {
        this.page = page;
        this.privilege = privilege;
    }
}
