package com.springsecurity.rbac.springsecurityrbac.service;

import com.springsecurity.rbac.springsecurityrbac.entity.User;
import com.springsecurity.rbac.springsecurityrbac.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    private User findById(long id) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (Optional.of(user).isEmpty()) {
            throw new UsernameNotFoundException("User with {}" + id + " does not exists!");
        }
        return user.get();
    }

    private List<User> findAll() {
        return userRepository.findAll();
    }

    private void removeById(long id) {
        userRepository.deleteById(id);
    }

}
