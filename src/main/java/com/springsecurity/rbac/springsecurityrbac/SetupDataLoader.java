package com.springsecurity.rbac.springsecurityrbac;

import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE;
import com.springsecurity.rbac.springsecurityrbac.entity.contsants.PRIVILEGE;
import com.springsecurity.rbac.springsecurityrbac.entity.security.*;
import com.springsecurity.rbac.springsecurityrbac.service.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private RoleService roleService;
    private PrivilegeService privilegeService;

    private PagesPrivilegesService pagesPrivilegesService;
    private UserService userService;
    private PageService pageService;

    private RolePagesPrivilegesService rolePagesPrivilegesService;

    public SetupDataLoader(RoleService roleService, PrivilegeService privilegeService,
                           PagesPrivilegesService pagesPrivilegesService,
                           UserService userService, PageService pageService, RolePagesPrivilegesService rolePagesPrivilegesService) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.pagesPrivilegesService = pagesPrivilegesService;
        this.userService = userService;
        this.pageService = pageService;
        this.rolePagesPrivilegesService = rolePagesPrivilegesService;
    }

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //setup default privileges
        privilegeService.save(new Privilege(PRIVILEGE.CREATE));
        privilegeService.save(new Privilege(PRIVILEGE.READ));
        privilegeService.save(new Privilege(PRIVILEGE.WRITE));
        privilegeService.save(new Privilege(PRIVILEGE.UPDATE));
        privilegeService.save(new Privilege(PRIVILEGE.DELETE));

        pageService.save(new Page(PAGE.HOME));
        pageService.save(new Page(PAGE.SUMMARY));
        pageService.save(new Page(PAGE.ORDER));
        pageService.save(new Page(PAGE.PRODUCT));


        //prepare all privileges for root user
        List<Page> pageList = pageService.findAll();
        List<Privilege> privilegeList = privilegeService.findAll();

        List<PagesPrivileges> pagesPrivilegesList = new ArrayList<>();

        for (Page page : pageList) {
            for (Privilege privilege : privilegeList) {
                PagesPrivileges pagesPrivileges = new PagesPrivileges();
                pagesPrivileges.setPage(page);
                pagesPrivileges.setPrivilege(privilege);
                pagesPrivilegesList.add(pagesPrivilegesService.save(pagesPrivileges));
            }
        }
        //root admin role
        Role adminRole = new Role("ADMIN");
        adminRole = roleService.save(adminRole);

        for (PagesPrivileges pagesPrivileges : pagesPrivilegesList) {
            RolePagesPrivileges rolePagesPrivileges = new RolePagesPrivileges();
            rolePagesPrivileges.setPagesPrivileges(pagesPrivileges);
            rolePagesPrivileges.setRole(adminRole);
            rolePagesPrivilegesService.save(rolePagesPrivileges);
        }

        //setup default root admin
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@test.com");
        admin.setRoles(List.of(adminRole));
        admin.setEnabled(true);
        userService.save(admin);
    }
}
