package com.springsecurity.rbac.springsecurityrbac.controller;

import com.springsecurity.rbac.springsecurityrbac.dto.RoleDto;
import com.springsecurity.rbac.springsecurityrbac.dto.UserDto;
import com.springsecurity.rbac.springsecurityrbac.entity.security.AssignRole;
import com.springsecurity.rbac.springsecurityrbac.exception.RoleAlreadyExistException;
import com.springsecurity.rbac.springsecurityrbac.exception.RoleNotFoundException;
import com.springsecurity.rbac.springsecurityrbac.service.RoleService;
import com.springsecurity.rbac.springsecurityrbac.mapper.RoleMapper;
import org.springframework.http.HttpStatus;
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
    public Collection<RoleDto> findAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/findByName")
    public RoleDto findRoleByName(@RequestParam String name) {
        try {
            return RoleMapper.toRoleDto(roleService.findByName(name));
        } catch (RoleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/create")
    public RoleDto createRole(@RequestBody RoleDto roleDto) {
        try {
            return roleService.createRole(roleDto);
        } catch (RoleAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/assignRole")
    public UserDto assignRole(@RequestBody AssignRole assignRole) {
        try {
            return roleService.assignRole(assignRole);
        } catch (RoleNotFoundException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update")
    public RoleDto updateRole(@RequestBody RoleDto roleDto) {
        try {
            return roleService.updateRole(roleDto);
        } catch (RoleNotFoundException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
