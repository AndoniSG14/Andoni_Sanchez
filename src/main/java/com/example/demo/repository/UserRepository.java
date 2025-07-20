package com.example.demo.repository;

import com.example.demo.model.Address;
import com.example.demo.model.input.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.of("Europe/London"));

        for (Long i = 1L; i < 4; i++) {
            users.add(new User(i, (4-i)+"automaticemail@mail.com", i+"User_",
                    sha1("test"+i), formatter.format(Instant.parse("2025-0"+(10-i)+"-01T00:00:00Z")),
                    Arrays.asList(
                            new Address(i, "Office"+i, "street No. "+i, "Contry_"+i),
                            new Address(i+1, "Home"+i, "street No. "+i, "Contry_"+(i+i))
                    )));
        }

    }

    public List<User> getAll() {
        return users;
    }

    public Optional<User> getById(Long id) {
        return users.stream().filter((user) -> user.getId().equals(id)).findFirst();
    }

    public void save(User user) {
        users.add(user);
    }

    public void delete(Long id) {
        users.removeIf((user) -> user.getId().equals(id));
    }

    private String sha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
