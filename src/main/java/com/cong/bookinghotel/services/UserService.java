package com.cong.bookinghotel.services;

import com.cong.bookinghotel.exceptions.UserAlreadyExistsException;
import com.cong.bookinghotel.models.Role;
import com.cong.bookinghotel.models.User;
import com.cong.bookinghotel.repositories.RoleRepository;
import com.cong.bookinghotel.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role userRole = new Role();
        if (roleRepository.findByName("ROLE_USER").isPresent()) {
            userRole = roleRepository.findByName("ROLE_USER").get();
        } else if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}