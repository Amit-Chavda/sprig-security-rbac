package com.springsecurity.rbac.springsecurityrbac.controller;

import com.springsecurity.rbac.springsecurityrbac.dto.UserDto;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.entity.security.*;
import com.springsecurity.rbac.springsecurityrbac.repository.PageRepository;
import com.springsecurity.rbac.springsecurityrbac.service.*;
import com.springsecurity.rbac.springsecurityrbac.util.PrivilegeUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {


    private PageService pageService;
    private PrivilegeService privilegeService;
    private UserService userService;
    private PageRepository pageRepository;
    private PagesPrivilegesService pagesPrivilegesService;

    private RoleService roleService;

    private RolePagesPrivilegesService rolePagesPrivilegesService;

    public UserController(PageService pageService, PrivilegeService privilegeService, UserService userService, PageRepository pageRepository, PagesPrivilegesService pagesPrivilegesService, RoleService roleService, RolePagesPrivilegesService rolePagesPrivilegesService) {
        this.pageService = pageService;
        this.privilegeService = privilegeService;
        this.userService = userService;
        this.pageRepository = pageRepository;
        this.pagesPrivilegesService = pagesPrivilegesService;
        this.roleService = roleService;
        this.rolePagesPrivilegesService = rolePagesPrivilegesService;
    }

    @PostMapping("/create")
    public UserDto createUser(@RequestBody UserDto userDto) {

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());

        List<Page> pages = userDto.getRoles().stream().flatMap(roleDto -> roleDto.getPages().stream()).map(pageDto -> {
            Page p = new Page();
            p.setName(pageDto.getName());
            p.setPrivileges(PrivilegeUtil.toPrivileges(pageDto.getPrivileges()));
            return p;
        }).toList();

        List<PagesPrivileges> pagesPrivilegesList = new ArrayList<>();

        for (Page page : pages) {
            for (Privilege privilege : page.getPrivileges()) {
                PagesPrivileges pagesPrivileges = new PagesPrivileges();
                pagesPrivileges.setPage(pageService.save(page));
                pagesPrivileges.setPrivilege(privilegeService.save(privilege));
                pagesPrivilegesList.add(pagesPrivilegesService.save(pagesPrivileges));
            }
        }


        Set<Role> roleHashSet = new HashSet<>();
        userDto.getRoles().stream().forEach(roleDto -> {
            Role role = new Role();
            role.setName(roleDto.getName());
            for (PagesPrivileges pagesPrivileges : pagesPrivilegesList) {
                RolePagesPrivileges rolePagesPrivileges = new RolePagesPrivileges();
                rolePagesPrivileges.setPagesPrivileges(pagesPrivileges);
                role = roleService.save(role);
                roleHashSet.add(role);
                rolePagesPrivileges.setRole(role);
                rolePagesPrivilegesService.save(rolePagesPrivileges);
            }
        });

        //set roles
        user.setRoles(roleHashSet);
        //save users
        userService.save(user);
        return userDto;
    }


}
