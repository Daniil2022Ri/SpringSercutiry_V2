package ru.kata.spring.boot_security.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            System.out.println("Received user: " + user); // Debug
            User createdUser = userService.createNewUserRest(user);
            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage()); // Debug
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsersRest(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userService.findByUsername(principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.updateUserRest(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}