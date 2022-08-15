package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.dto.RoleDto;
import com.springsecurity.rbac.springsecurityrbac.dto.UserDto;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.entity.security.*;
import com.springsecurity.rbac.springsecurityrbac.exception.RoleAlreadyExistException;
import com.springsecurity.rbac.springsecurityrbac.exception.RoleNotFoundException;
import com.springsecurity.rbac.springsecurityrbac.repository.RoleRepository;
import com.springsecurity.rbac.springsecurityrbac.mapper.RoleMapper;
import com.springsecurity.rbac.springsecurityrbac.mapper.UserMapper;
import com.springsecurity.rbac.springsecurityrbac.util.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    private final UserService userService;

    private final PageService pageService;
    private final PrivilegeService privilegeService;
    private final PagesPrivilegesService pagesPrivilegesService;

    private final RolePagesPrivilegesService rolePagesPrivilegesService;

    public RoleService(RoleRepository roleRepository, UserService userService, PageService pageService, PrivilegeService privilegeService,
                       PagesPrivilegesService pagesPrivilegesService, RolePagesPrivilegesService rolePagesPrivilegesService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.pageService = pageService;
        this.privilegeService = privilegeService;
        this.pagesPrivilegesService = pagesPrivilegesService;
        this.rolePagesPrivilegesService = rolePagesPrivilegesService;
    }

    public Role findByName(String name) throws RoleNotFoundException {
        Optional<Role> optionalRole = Optional.ofNullable(roleRepository.findByName(name));
        if (optionalRole.isEmpty()) {
            throw new RoleNotFoundException(RoleAlreadyExistException.class.getName(),
                    "Role " + name + " does not exist!", LocalDateTime.now());
        }
        return optionalRole.get();
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public RoleDto createRole(RoleDto roleDto) throws RoleAlreadyExistException {
        Console.println(roleRepository.existsByName(roleDto.getName()) + "", RoleService.class);

        if (roleRepository.existsByName(roleDto.getName())) {
            throw new RoleAlreadyExistException(RoleAlreadyExistException.class.getName(),
                    "Role " + roleDto.getName() + " already exist!", LocalDateTime.now());
        }

        Role role = save(new Role(roleDto.getName()));
        Collection<RolePagesPrivileges> rolePagesPrivilegesList = RoleMapper.toRole(roleDto).getRolePagesPrivileges().stream().map(rolePagesPrivileges -> {

            Page page = pageService.save(rolePagesPrivileges.getPagesPrivileges().getPage());
            Privilege privilege = privilegeService.save(rolePagesPrivileges.getPagesPrivileges().getPrivilege());

            PagesPrivileges pagesPrivileges = pagesPrivilegesService.save(new PagesPrivileges(page, privilege));

            rolePagesPrivileges.setPagesPrivileges(pagesPrivileges);
            rolePagesPrivileges.setRole(role);
            return rolePagesPrivilegesService.save(rolePagesPrivileges);

        }).toList();
        role.setRolePagesPrivileges(new ArrayList<>(rolePagesPrivilegesList));
        logger.info("New role with name {} is created!", role.getName());
        return RoleMapper.toRoleDto(roleRepository.save(role));
    }

    public UserDto assignRole(AssignRole assignRole) throws UsernameNotFoundException, RoleNotFoundException {

        List<Role> roles = assignRole.getRoleNames().stream().map(roleRepository::findByName).toList();

        User user = userService.findByEmail(assignRole.getUsername());

        //user.setRoles(roles); //java.lang.UnsupportedOperationException: null
        user.setRoles(new ArrayList<>(roles));
        logger.info("New role {} assigned to user {}", assignRole.getRoleNames(), assignRole.getUsername());
        return UserMapper.toUserDto(userService.save(user));
    }

    public Collection<RoleDto> findAll() {
        return RoleMapper.toRoleDtos(roleRepository.findAll());
    }

    public RoleDto updateRole(RoleDto roleDto) {
        //delete existing role
        Role role = findByName(roleDto.getName());
        delete(role);
        //create new role and return
        return createRole(roleDto);
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
