package com.springsecurity.rbac.springsecurityrbac;

import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE;
import com.springsecurity.rbac.springsecurityrbac.entity.security.*;
import com.springsecurity.rbac.springsecurityrbac.repository.UserRepository;
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
    private UserRepository userRepository;
    private PageService pageService;

    private RolePagesPrivilegesService rolePagesPrivilegesService;

    public SetupDataLoader(RoleService roleService, PrivilegeService privilegeService,
                           PagesPrivilegesService pagesPrivilegesService, UserRepository userRepository,
                           PageService pageService, RolePagesPrivilegesService rolePagesPrivilegesService) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.pagesPrivilegesService = pagesPrivilegesService;
        this.userRepository = userRepository;
        this.pageService = pageService;
        this.rolePagesPrivilegesService = rolePagesPrivilegesService;
    }

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege createPrivilege = createPrivilegeIfNotFound("CREATE_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege updatePrivilege = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        //create page
        Page homePage = createPageIfNotFound(PAGE.HOME);
        Page productPage = createPageIfNotFound(PAGE.PRODUCT);
        Page orderPage = createPageIfNotFound(PAGE.ORDER);
        Page summaryPage = createPageIfNotFound(PAGE.SUMMARY);


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
        Role adminRole = new Role("ADMIN");
        adminRole = roleService.save(adminRole);

        for (PagesPrivileges pagesPrivileges : pagesPrivilegesList) {
            RolePagesPrivileges rolePagesPrivileges = new RolePagesPrivileges();
            rolePagesPrivileges.setPagesPrivileges(pagesPrivileges);
            rolePagesPrivileges.setRole(adminRole);
            rolePagesPrivilegesService.save(rolePagesPrivileges);
        }

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@test.com");
        admin.setRoles(List.of(adminRole));
        admin.setEnabled(true);
        userRepository.save(admin);
    }

    @Transactional
    Page createPageIfNotFound(String name) {
        Page page = pageService.findByName(name);
        if (page == null) {
            page = new Page(name);
            pageService.save(page);
        }
        return page;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeService.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeService.save(privilege);
        }
        return privilege;
    }
}
