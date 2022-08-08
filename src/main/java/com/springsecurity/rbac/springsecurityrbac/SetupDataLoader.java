package com.springsecurity.rbac.springsecurityrbac;

import com.springsecurity.rbac.springsecurityrbac.entity.Privilege;
import com.springsecurity.rbac.springsecurityrbac.entity.Role;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.repository.UserRepository;
import com.springsecurity.rbac.springsecurityrbac.service.PrivilegeService;
import com.springsecurity.rbac.springsecurityrbac.service.RoleService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
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
    }
}
