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
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    private Collection<RolePagesPrivileges> rolePagesPrivileges;

    public Role(String name) {
        this.name = name;
    }

}
