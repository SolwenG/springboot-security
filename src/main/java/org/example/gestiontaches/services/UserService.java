package org.example.gestiontaches.services;

import org.example.gestiontaches.models.Users;
import org.example.gestiontaches.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Users.Role getRoleByUsername(String username) {
        Users user = userRepository.findByUsername(username);
        return (user != null) ? user.getRole() : null;
    }
}
