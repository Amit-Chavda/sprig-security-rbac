package com.springsecurity.rbac.springsecurityrbac;

import com.springsecurity.rbac.springsecurityrbac.entity.security.*;
import com.springsecurity.rbac.springsecurityrbac.repository.PagesPrivilegesRepository;
import com.springsecurity.rbac.springsecurityrbac.repository.RolePagesPrivilegesRepository;
import com.springsecurity.rbac.springsecurityrbac.service.PageService;
import com.springsecurity.rbac.springsecurityrbac.service.PagesPrivilegesService;
import com.springsecurity.rbac.springsecurityrbac.service.PrivilegeService;
import com.springsecurity.rbac.springsecurityrbac.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private RoleService roleService;
    private PrivilegeService privilegeService;

    @Autowired
    private PagesPrivilegesService pagesPrivilegesService;
    private PageService pageService;

    private RolePagesPrivilegesRepository rolePagesPrivilegesRepository;
    private PagesPrivilegesRepository pagesPrivilegesRepository;

    public SetupDataLoader(RoleService roleService, PrivilegeService privilegeService, PageService pageService, RolePagesPrivilegesRepository rolePagesPrivilegesRepository, PagesPrivilegesRepository pagesPrivilegesRepository) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.pageService = pageService;
        this.rolePagesPrivilegesRepository = rolePagesPrivilegesRepository;
        this.pagesPrivilegesRepository = pagesPrivilegesRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege createPrivilege = createPrivilegeIfNotFound("CREATE_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege updatePrivilege = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        //create page
        Page homePage = createPageIfNotFound(com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE.HOME);
        Page productPage = createPageIfNotFound(com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE.PRODUCT);
        Page orderPage = createPageIfNotFound(com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE.ORDER);


        List<Page> pageList = pageService.findAll();
        List<Privilege> privilegeList = privilegeService.findAll();

        List<PagesPrivileges> pagesPrivilegesList = new ArrayList<>();

        for (Page page : pageList) {
            for (Privilege privilege : privilegeList) {
                PagesPrivileges pagesPrivileges = new PagesPrivileges();
                pagesPrivileges.setPage(page);
                pagesPrivileges.setPrivilege(privilege);

                Optional<PagesPrivileges> pagesPrivilegesOptional = pagesPrivilegesService.alreadyExists(pagesPrivileges);

                if (pagesPrivilegesOptional.isPresent()) {
                    pagesPrivilegesList.add(pagesPrivilegesOptional.get());
                } else {
                    pagesPrivilegesList.add(pagesPrivilegesRepository.save(pagesPrivileges));
                }
            }
        }

        for (PagesPrivileges pagesPrivileges : pagesPrivilegesList) {
            RolePagesPrivileges rolePagesPrivileges = new RolePagesPrivileges();
            rolePagesPrivileges.setPagesPrivileges(pagesPrivileges);
            rolePagesPrivileges.setRole(createRoleIfNotFound("ADMIN2"));
            rolePagesPrivilegesRepository.save(rolePagesPrivileges);
        }


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

    @Transactional
    Role createRoleIfNotFound(String name, Collection<PagesPrivileges> pagesPrivileges) {
        Role role = roleService.findByName(name);
        if (role == null) {
            role = new Role(name);
//            role.setPagesPrivileges(pagesPrivileges);
            roleService.save(role);
        }
        return role;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        Role role = roleService.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleService.save(role);
        }
        return role;
    }
}
