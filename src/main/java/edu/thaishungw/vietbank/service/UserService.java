package edu.thaishungw.vietbank.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.thaishungw.vietbank.models.Users;
import edu.thaishungw.vietbank.repo.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users findByUserEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<Users> getAllUsers() {
        return userRepository.getAllUser();
    }
}
