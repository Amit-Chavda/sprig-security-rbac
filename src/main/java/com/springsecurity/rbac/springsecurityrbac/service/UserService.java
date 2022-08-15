package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.dto.UserDto;
import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.exception.UserAlreadyExistException;
import com.springsecurity.rbac.springsecurityrbac.repository.UserRepository;
import com.springsecurity.rbac.springsecurityrbac.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public UserDto createUser(UserDto userDto) throws UserAlreadyExistException {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistException(UserAlreadyExistException.class.getName(),
                    "User with " + userDto.getEmail() + " already exist!", LocalDateTime.now());
        }
        return UserMapper.toUserDto(save(UserMapper.toUser(userDto)));

    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public User findByEmail(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with email {}" + username + " does not exists!");
        }
        return optionalUser.get();
    }

    public UserDto deleteByEmail(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        userRepository.delete(user);
        return UserMapper.toUserDto(user);
    }

/*    public UserDto createUser(UserDto userDto) throws UserAlreadyExistException {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistException(UserAlreadyExistException.class.getName(), "User with " + userDto.getEmail() + " already exist!", LocalDateTime.now());
        }

        Set<Role> roleSet = userDto.getRoles().stream()
                .map(roleDto -> {
                    List<PagesPrivileges> pagesPrivilegesList = roleDto.getPagePrivilegeMap().entrySet()
                            .stream()
                            .map(pageDtoListEntry -> {

                                Page page = new Page(pageDtoListEntry.getKey().getName());

                                List<Privilege> privileges = pageDtoListEntry.getValue().stream()
                                        .map(privilegeDto ->
                                                new Privilege(privilegeDto.getName())
                                        ).toList();

                                List<PagesPrivileges> pagesPrivilegesList1 = new ArrayList<>();

                                for (Privilege privilege : privileges) {
                                    PagesPrivileges pagesPrivileges = new PagesPrivileges();
                                    pagesPrivileges.setPage(pageService.save(page));
                                    pagesPrivileges.setPrivilege(privilegeService.save(privilege));
                                    pagesPrivilegesList1.add(pagesPrivilegesService.save(pagesPrivileges));
                                }

                                return pagesPrivilegesList1;
                            })
                            .toList()
                            .stream()
                            .flatMap(List::stream)
                            .toList();

                    return pagesPrivilegesList.stream()
                            .map(pagesPrivileges -> {
                                Role role = new Role();
                                role.setName(roleDto.getName());

                                RolePagesPrivileges rolePagesPrivileges = new RolePagesPrivileges();

                                //role already exists then just assign existing role
                                //else create new and then assign
                                if (roleService.findByName(role.getName()).isEmpty()) {
                                    role = roleService.save(role);
                                    rolePagesPrivileges.setPagesPrivileges(pagesPrivileges);
                                } else {
                                    role = roleService.save(role);
                                    rolePagesPrivileges.setPagesPrivileges(rolePagesPrivileges.getPagesPrivileges());
                                }
                                rolePagesPrivileges.setRole(role);
                                rolePagesPrivilegesService.save(rolePagesPrivileges);
                                return role;
                            }).collect(Collectors.toSet());

                })
                .collect(Collectors.toSet())
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
        userRepository.save(user);
        return userDto;
    }*/


    /*

    Example of Object-Oriented Programming

    public UserDto createUser(UserDto userDto) {

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
}
