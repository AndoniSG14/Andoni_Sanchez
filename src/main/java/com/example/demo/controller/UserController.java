package com.example.demo.controller;

import com.example.demo.model.Address;
import com.example.demo.model.input.User;
import com.example.demo.model.output.UserOutput;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserOutput> getUsers(@RequestParam(required = false) String sortedBy) {
        return userService.getUsers(sortedBy);
    }

    @GetMapping("/{userId}/addresses")
    public List<Address> getAddresses(@PathVariable Long userId) {
        return userService.getAddresses(userId);
    }

    @PutMapping("/{userId}/addresses/{addressId}")
    public Address updateAddress(@PathVariable Long userId,
                                 @PathVariable Long addressId,
                                 @RequestBody Address address) {
        return userService.updateAddress(userId, addressId, address);
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public User patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return userService.updateuser(id, updates);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
