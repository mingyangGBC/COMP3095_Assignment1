package com.example.comp3095_assignment1.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.example.comp3095_assignment1.dto.RegisterDto;
import com.example.comp3095_assignment1.model.Role;
import com.example.comp3095_assignment1.model.User;
import com.example.comp3095_assignment1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User save(RegisterDto register){
        User user = new User();
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setUsername(register.getUsername());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
