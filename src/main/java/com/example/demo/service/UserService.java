package com.example.demo.service;

import com.example.demo.model.Address;
import com.example.demo.model.input.User;
import com.example.demo.model.output.UserOutput;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserOutput> getUsers(String sortedBy) {
        if (sortedBy == null || sortedBy.isBlank()){  sortedBy = "";}

        Comparator<User> comparator = switch (sortedBy) {
            case "email" -> Comparator.comparing(User::getEmail);
            case "id" -> Comparator.comparing(User::getId);
            case "name" -> Comparator.comparing(User::getName);
            case "created_at" -> Comparator.comparing(User::getCreatedAt);
            default -> (u1, u2) -> 0;
        };
        List<User> userInput = userRepository.getAll().stream().sorted(comparator).toList();
        List<UserOutput> userOutput =  new ArrayList<>();
        for  (User user : userInput) {
            UserOutput newUserOutput = new UserOutput();
            newUserOutput.setId(user.getId());
            newUserOutput.setEmail(user.getEmail());
            newUserOutput.setName(user.getName());
            newUserOutput.setCreatedAt(user.getCreatedAt());
            newUserOutput.setAddresses(user.getAddresses());
            userOutput.add(newUserOutput);
        }

        return userOutput;
    }

    public List<Address> getAddresses(Long userId) {
        return userRepository.getById(userId).map(User::getAddresses).orElseThrow();
    }

    public Address updateAddress(Long userId, Long addressId, Address newAddress) {
        User user = userRepository.getById(userId).orElseThrow();
        for (int i = 0; i < user.getAddresses().size(); i++) {
            if (user.getAddresses().get(i).getId().equals(addressId)) {
                newAddress.setId(addressId);
                user.getAddresses().set(i, newAddress);
                return newAddress;
            }
        }
        throw new NoSuchElementException("Address not found");
    }

    public void addUser(User user) {
        user.setCreatedAt(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                .withZone(ZoneId.of("Europe/London")).format(Instant.now()));
        user.setPassword(sha1(user.getPassword()));

        userRepository.save(user);
    }

    public User updateuser(Long userId, Map<String, Object> updates) {
        User user = userRepository.getById(userId).orElseThrow();
        updates.forEach((k, v) -> {
            switch (k) {
                case "email" -> user.setEmail((String) v);
                case "name" -> user.setName((String) v);
                case "password" -> user.setPassword(sha1((String) v));
                case "created_at" -> user.setCreatedAt((String) v);
            }
        });
        return user;
    }

    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }

    private String sha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
