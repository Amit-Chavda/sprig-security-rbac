package com.springsecurity.rbac.springsecurityrbac;

import com.springsecurity.rbac.springsecurityrbac.dto.PageDto;
import com.springsecurity.rbac.springsecurityrbac.dto.PrivilegeDto;
import com.springsecurity.rbac.springsecurityrbac.dto.RoleDto;
import com.springsecurity.rbac.springsecurityrbac.dto.UserDto;
import com.springsecurity.rbac.springsecurityrbac.entity.contsants.PAGE;
import com.springsecurity.rbac.springsecurityrbac.entity.contsants.PRIVILEGE;
import com.springsecurity.rbac.springsecurityrbac.entity.security.AssignRole;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Page;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Privilege;
import com.springsecurity.rbac.springsecurityrbac.entity.security.Role;
import com.springsecurity.rbac.springsecurityrbac.exception.UserAlreadyExistException;
import com.springsecurity.rbac.springsecurityrbac.service.PageService;
import com.springsecurity.rbac.springsecurityrbac.service.PrivilegeService;
import com.springsecurity.rbac.springsecurityrbac.service.RoleService;
import com.springsecurity.rbac.springsecurityrbac.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);
    private PrivilegeService privilegeService;

    private UserService userService;
    private PageService pageService;

    private RoleService roleService;

    public SetupDataLoader(PrivilegeService privilegeService, UserService userService, PageService pageService, RoleService roleService) {
        this.privilegeService = privilegeService;
        this.userService = userService;
        this.pageService = pageService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //setup default privileges
        privilegeService.save(new Privilege(PRIVILEGE.CREATE));
        privilegeService.save(new Privilege(PRIVILEGE.READ));
        privilegeService.save(new Privilege(PRIVILEGE.WRITE));
        privilegeService.save(new Privilege(PRIVILEGE.UPDATE));
        privilegeService.save(new Privilege(PRIVILEGE.DELETE));

        //setup default pages
        pageService.save(new Page(PAGE.HOME));
        pageService.save(new Page(PAGE.SUMMARY));
        pageService.save(new Page(PAGE.ORDER));
        pageService.save(new Page(PAGE.PRODUCT));

        //prepare all privileges for root user
        HashMap<PageDto, List<PrivilegeDto>> adminRole = new HashMap<>();

        List<PrivilegeDto> prList = privilegeService.findAll()
                .stream()
                .map(privilege -> new PrivilegeDto(privilege.getName()))
                .toList();

        List<PageDto> pageList = pageService.findAll()
                .stream()
                .map(page -> new PageDto(page.getName()))
                .toList();

        for (PageDto pageDto : pageList) {
            adminRole.put(pageDto, prList);
        }

        RoleDto adminRoleDto = new RoleDto("ADMIN", adminRole);

        try {
            roleService.createRole(adminRoleDto);
        } catch (Exception e) {
            logger.info(e.toString());
        }


        UserDto admin = new UserDto();
        try {
            //setup default root admin

            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            admin.setEmail("admin@test.com");
            admin.setRoles(List.of(adminRoleDto));
            admin.setEnabled(true);
            userService.createUser(admin);

        } catch (Exception e) {
            logger.info(e.toString());
        }
        try {
            AssignRole assignRole = new AssignRole();
            assignRole.setUsername(admin.getEmail());
            assignRole.setRoleNames(new ArrayList<>(Collections.singleton(adminRoleDto.getName())));
            roleService.assignRole(assignRole);
        } catch (Exception e) {
            logger.info(e.toString());
        }

    }
}
