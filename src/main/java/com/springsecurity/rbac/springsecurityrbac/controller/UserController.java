package com.springsecurity.rbac.springsecurityrbac.controller;

import com.springsecurity.rbac.springsecurityrbac.dto.PageDto;
import com.springsecurity.rbac.springsecurityrbac.dto.PrivilegeDto;
import com.springsecurity.rbac.springsecurityrbac.dto.RoleDto;
import com.springsecurity.rbac.springsecurityrbac.dto.UserDto;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.entity.security.*;
import com.springsecurity.rbac.springsecurityrbac.repository.PageRepository;
import com.springsecurity.rbac.springsecurityrbac.security.JdbcRoleChecker;
import com.springsecurity.rbac.springsecurityrbac.service.*;
import com.springsecurity.rbac.springsecurityrbac.util.AuthorityUtil;
import com.springsecurity.rbac.springsecurityrbac.util.Console;
import com.springsecurity.rbac.springsecurityrbac.util.PrivilegeUtil;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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
/*
    Object Oriented programming
    */
 /*
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
    */

    /*
        Functional programming
        */
    @PostMapping("/create")
    public UserDto createUser(@RequestBody UserDto userDto) {


        Set<Role> roleSet = userDto.getRoles().stream()
                .map(
                        roleDto -> {
                            List<PagesPrivileges> pagesPrivilegesList = roleDto.getPagePrivilegeMap().entrySet()
                                    .stream().map(pageDtoListEntry -> {

                                        Page page = new Page(pageDtoListEntry.getKey().getName());

                                        List<Privilege> privileges = pageDtoListEntry.getValue().stream().map(privilegeDto -> new Privilege(privilegeDto.getName())).toList();

                                        List<PagesPrivileges> pagesPrivilegesList1 = new ArrayList<>();

                                        for (Privilege privilege : privileges) {
                                            PagesPrivileges pagesPrivileges = new PagesPrivileges();
                                            pagesPrivileges.setPage(pageService.save(page));
                                            pagesPrivileges.setPrivilege(privilegeService.save(privilege));
                                            pagesPrivilegesList1.add(pagesPrivilegesService.save(pagesPrivileges));
                                        }

                                        return pagesPrivilegesList1;
                                    }).toList()
                                    .stream()
                                    .flatMap(List::stream)
                                    .toList();

                            return pagesPrivilegesList.stream()
                                    .map(pagesPrivileges -> {
                                        Role role = new Role();
                                        role.setName(roleDto.getName());
                                        role = roleService.save(role);

                                        RolePagesPrivileges rolePagesPrivileges = new RolePagesPrivileges();
                                        rolePagesPrivileges.setPagesPrivileges(pagesPrivileges);
                                        rolePagesPrivileges.setRole(role);
                                        rolePagesPrivilegesService.save(rolePagesPrivileges);

                                        return role;
                                    }).collect(Collectors.toSet());

                        }).collect(Collectors.toSet())
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        //set roles
        user.setRoles(roleSet);
        //save users
        userService.save(user);
        return userDto;
    }


    @GetMapping("test")
    @PreAuthorize(value = "@roleChecker.check(authentication,#request)")
    public List<RoleDto> test( HttpServletRequest request) {
        JdbcRoleChecker jdbcRoleChecker = new JdbcRoleChecker();

        // Console.println("Accessible :" + jdbcRoleChecker.check(SecurityContextHolder.getContext().getAuthentication(), request) + "", UserController.class);
        ;
        User user = userService.findAll().get(0);
        //AuthorityUtil.getAllGrantedAuthorities(user).stream().forEach(System.out::println);

        return AuthorityUtil.getRoleAndAuthorities(user);
    }

    @PostMapping("test")
    @PreAuthorize(value = "@roleChecker.check(authentication,#request)")
    public List<RoleDto> testPost( HttpServletRequest request) {
        JdbcRoleChecker jdbcRoleChecker = new JdbcRoleChecker();

        // Console.println("Accessible :" + jdbcRoleChecker.check(SecurityContextHolder.getContext().getAuthentication(), request) + "", UserController.class);
        ;
        User user = userService.findAll().get(0);
        //AuthorityUtil.getAllGrantedAuthorities(user).stream().forEach(System.out::println);

        return AuthorityUtil.getRoleAndAuthorities(user);
    }
}
