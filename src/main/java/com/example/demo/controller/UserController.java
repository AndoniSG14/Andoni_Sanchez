package com.example.demo.controller;

import com.example.demo.model.Address;
import com.example.demo.model.input.User;
import com.example.demo.model.output.UserOutput;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations on users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get user list", description = "Returns all users sorted by a selected field")
    @ApiResponse(responseCode = "200", description = "User list successfully obtained")
    @GetMapping
    public List<UserOutput> getUsers(@RequestParam(required = false) String sortedBy) {
        return userService.getUsers(sortedBy);
    }

    @Operation(summary = "List of user addresses", description = "Gets the list of addresses of the selected user")
    @ApiResponse(responseCode = "200", description = "Address list obtained successfully")
    @GetMapping("/{userId}/addresses")
    public List<Address> getAddresses(@PathVariable Long userId) {
        return userService.getAddresses(userId);
    }

    @Operation(summary = "Address update", description = "Modify the address of the selected User and your selected address")
    @ApiResponse(responseCode = "200", description = "Address successfully modified")
    @PutMapping("/{userId}/addresses/{addressId}")
    public Address updateAddress(@PathVariable Long userId,
                                 @PathVariable Long addressId,
                                 @RequestBody Address address) {
        return userService.updateAddress(userId, addressId, address);
    }

    @Operation(summary = "User Creation", description = "New user registration and their addresses")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @Operation(summary = "User modification", description = "Modification of user information such as: Name, Email, Password or all of them")
    @ApiResponse(responseCode = "200", description = "User modified successfully")
    @PatchMapping("/{id}")
    public User patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return userService.updateuser(id, updates);
    }

    @Operation(summary = "Delete User", description = "Delete selected user")
    @ApiResponse(responseCode = "200", description = "User successfully deleted")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
