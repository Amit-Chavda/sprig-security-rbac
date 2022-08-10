package com.springsecurity.rbac.springsecurityrbac;

import com.springsecurity.rbac.springsecurityrbac.dto.PageDto;
import com.springsecurity.rbac.springsecurityrbac.dto.PrivilegeDto;
import com.springsecurity.rbac.springsecurityrbac.dto.RoleDto;
import com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE;
import com.springsecurity.rbac.springsecurityrbac.entity.contsants.PRIVILEGE;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Page;
import com.springsecurity.rbac.springsecurityrbac.entity.security.PagesPrivileges;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Privilege;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Role;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.repository.PagesPrivilegesRepository;
import com.springsecurity.rbac.springsecurityrbac.repository.UserRepository;
import com.springsecurity.rbac.springsecurityrbac.service.PageService;
import com.springsecurity.rbac.springsecurityrbac.service.PrivilegeService;
import com.springsecurity.rbac.springsecurityrbac.service.RoleService;
import com.springsecurity.rbac.springsecurityrbac.util.PageUtil;
import com.springsecurity.rbac.springsecurityrbac.util.PrivilegeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;
    ModelMapper mapper = new ModelMapper();

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private UserRepository userRepository;
    private RoleService roleService;
    private PrivilegeService privilegeService;
    private PageService pageService;

    @Autowired
    private PagesPrivilegesRepository pagesPrivilegesRepository;

    public SetupDataLoader(UserRepository userRepository, RoleService roleService, PrivilegeService privilegeService, PageService pageService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.pageService = pageService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

      /*  Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege createPrivilege = createPrivilegeIfNotFound("CREATE_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege updatePrivilege = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");*/

        //create page
        Page homePage = createPageIfNotFound(com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE.HOME);
        Page productPage = createPageIfNotFound(com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE.PRODUCT);
        Page orderPage = createPageIfNotFound(com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE.ORDER);


        RoleDto roleDto = new RoleDto();

        Page page = createPageIfNotFound(PAGE.HOME);
        Privilege privilege = createPrivilegeIfNotFound(PRIVILEGE.READ);


        PagesPrivileges privileges = new PagesPrivileges();




        roleDto.setPages(
                Arrays.asList(
                        //page


                        new Page(
                                //page name
                                PAGE.HOME,
                                //allowed privileges
                                Arrays.asList(
                                        createPrivilegeIfNotFound(PRIVILEGE.READ),
                                        createPrivilegeIfNotFound(PRIVILEGE.WRITE),
                                        createPrivilegeIfNotFound(PRIVILEGE.DELETE),
                                        createPrivilegeIfNotFound(PRIVILEGE.UPDATE),
                                        createPrivilegeIfNotFound(PRIVILEGE.CREATE)
                                )
                        ),
                        new Page(
                                //page name
                                PAGE.HOME,
                                //allowed privileges
                                Arrays.asList(
                                        createPrivilegeIfNotFound(PRIVILEGE.READ),
                                        createPrivilegeIfNotFound(PRIVILEGE.READ),
                                        createPrivilegeIfNotFound(PRIVILEGE.READ),
                                        createPrivilegeIfNotFound(PRIVILEGE.READ)
                                )
                        )
                )
        );



        Role role = new Role("My Role");


        List<PagesPrivileges> pagesPrivilegesList = new ArrayList<>();

        //set page
        for (PageDto pageDto : roleDto.getPageDtos()) {
            PagesPrivileges pagesPrivileges = new PagesPrivileges();
            pagesPrivileges.setPage(PageUtil.toPage(pageDto));

            //set each privilege and save
            for (Privilege privilege : pageDto.getPrivileges()) {
                //set privilege
                pagesPrivileges.setPrivilege(privilege);
                PagesPrivileges pagesPrivilegesSaved = pagesPrivilegesRepository.save(pagesPrivileges);
                pagesPrivilegesList.add(pagesPrivilegesSaved);
            }
        }

        role = createRoleIfNotFound(role.getName(), pagesPrivilegesList);


        /*pageList.forEach(page -> {
            PagesPrivileges privileges = new PagesPrivileges();
            privileges.setPage(page);
            privilegeList.forEach(privilege -> {
                privileges.setPrivilege(privilege);
                privileges = pagesPrivilegesRepository.save(pagesPrivileges);
            });
            Role adminRole = createRoleIfNotFound("ROLE_ADMIN", List.of(pagesPrivileges));
        });


        PagesPrivileges pagesPrivileges1 = pagesPrivilegesRepository.save(pagesPrivileges);

        //create admin

        adminRole.setPagesPrivileges(new ArrayList<>(List.of(pagesPrivileges1)));

        adminRole = roleService.save(adminRole);

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@test.com");
        admin.setRoles(List.of(adminRole));
        admin.setEnabled(true);
        admin.setRoles(List.of(adminRole));

        userRepository.save(admin);*/

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
            role.setPagesPrivileges(pagesPrivileges);
            roleService.save(role);
        }
        return role;
    }

    /*    boolean alreadySetup = false;
    private UserRepository userRepository;
    private RoleService roleService;
    private PrivilegeService privilegeService;

    public SetupDataLoader(UserRepository userRepository, RoleService roleService, PrivilegeService privilegeService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.privilegeService = privilegeService;
    }

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (userRepository.existsById(1L)) return;

        //create privileges
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege updatePrivilege
                = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege deletePrivilege
                = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        //create privilege groups
        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege,
                updatePrivilege, deletePrivilege
        );
        List<Privilege> creatorPrivileges = List.of(readPrivilege, writePrivilege);
        List<Privilege> editorPrivileges = List.of(readPrivilege, updatePrivilege);
        List<Privilege> viewerPrivileges = List.of(readPrivilege);


        //create admin
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("admin@test.com");
        admin.setRoles(List.of(adminRole));
        admin.setEnabled(true);
        userRepository.save(admin);


        //create creator
        Role creatorRole = createRoleIfNotFound("ROLE_CREATOR", creatorPrivileges);
        User creator = new User();
        creator.setFirstName("creator");
        creator.setLastName("creator");
        creator.setPassword(passwordEncoder.encode("creator"));
        creator.setEmail("creator@test.com");
        creator.setRoles(List.of(creatorRole));
        creator.setEnabled(true);
        userRepository.save(creator);


        //create editor
        Role editorRole = createRoleIfNotFound("ROLE_EDITOR", editorPrivileges);
        User editor = new User();
        editor.setFirstName("editor");
        editor.setLastName("editor");
        editor.setPassword(passwordEncoder.encode("editor"));
        editor.setEmail("editor@test.com");
        editor.setRoles(List.of(editorRole));
        editor.setEnabled(true);
        userRepository.save(editor);


        //create viewer
        Role viewerRole = createRoleIfNotFound("ROLE_VIEWER", viewerPrivileges);
        User viewer = new User();
        viewer.setFirstName("viewer");
        viewer.setLastName("viewer");
        viewer.setPassword(passwordEncoder.encode("viewer"));
        viewer.setEmail("viewer@test.com");
        viewer.setRoles(List.of(viewerRole));
        viewer.setEnabled(true);
        userRepository.save(viewer);

        alreadySetup = true;
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
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleService.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleService.save(role);
        }
        return role;
    }*/
}
