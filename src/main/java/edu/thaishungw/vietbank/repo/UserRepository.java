package edu.thaishungw.vietbank.repo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import edu.thaishungw.vietbank.models.Users;

@Repository
public class UserRepository {
    private final Map<String, Users> userMap;

    public UserRepository() {
        userMap = new HashMap<>();
        userMap.put("19110221@student.hcmute.edu.vn",
                new Users("19110221@student.hcmute.edu.vn", "123456", "Nguyen Thai Hung"));
        userMap.put("18110221student.hcmute.edu.vn",
                new Users("18110221student@student.hcmute.edu.vn", "123456", "Nguyen Thai Hung"));
        userMap.put("17110221@student.hcmute.edu.vn",
                new Users("17110221@student.hcmute.edu.vn", "123456", "Nguyen Thai Hung"));
    }

    public Users findUserByEmail(String email) {
        return userMap.get(email);
    }

    public List<Users> getAllUser() {
        return List.copyOf(userMap.values());
    }
}
