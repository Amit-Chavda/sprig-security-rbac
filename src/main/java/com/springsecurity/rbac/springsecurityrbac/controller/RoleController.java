package com.springsecurity.rbac.springsecurityrbac.controller;

import com.springsecurity.rbac.springsecurityrbac.dto.*;
import com.springsecurity.rbac.springsecurityrbac.exception.RoleAlreadyExistException;
import com.springsecurity.rbac.springsecurityrbac.exception.RoleNotFoundException;
import com.springsecurity.rbac.springsecurityrbac.service.RoleService;
import com.springsecurity.rbac.springsecurityrbac.mapper.RoleMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@RequestMapping("/role")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("/findAll")
    @PreAuthorize(value = "@roleChecker.check(authentication)")
    public Collection<RoleDto> findAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/findByName")
    @PreAuthorize(value = "@roleChecker.check(authentication)")
    public RoleDto findRoleByName(@RequestParam String name) {
        try {
            return RoleMapper.toRoleDto(roleService.findByName(name));
        } catch (RoleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/create")
    @PreAuthorize(value = "@roleChecker.check(authentication)")
    public RoleDto createRole(@RequestBody RoleDto roleDto) {
        try {
            return roleService.createRole(roleDto);
        } catch (RoleAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/assignRole")
    @PreAuthorize(value = "@roleChecker.check(authentication)")
    public UserDto assignRole(@RequestBody AssignRole assignRole) {
        try {
            return roleService.assignRole(assignRole);
        } catch (RoleNotFoundException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/extendRole")
    @PreAuthorize(value = "@roleChecker.check(authentication)")
    public ExtendRole extendRole(@RequestBody ExtendRole extendRole) {

        try {
            return roleService.extendRole(extendRole);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/revokeExtendedPrivileges")
    @PreAuthorize(value = "@roleChecker.check(authentication)")
    public RevokeExtendPrivilege revokeExtendedPrivileges(@RequestBody RevokeExtendPrivilege revokeExtendPrivilege) {
        try {
            return roleService.revokeExtendedPrivileges(revokeExtendPrivilege);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/revokeRole")
    @PreAuthorize(value = "@roleChecker.check(authentication)")
    public UserDto revokeRole(@RequestBody RevokeRole revokeRole) {
        try {
            return roleService.revokeRole(revokeRole);
        } catch (RoleNotFoundException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update")
    @PreAuthorize(value = "@roleChecker.check(authentication)")
    public RoleDto updateRole(@RequestBody RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }
}
