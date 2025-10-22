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

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.createNewUserRest(userDTO) , HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUserPage(){
        return new ResponseEntity<>(userService.getAllUsersRest() , HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(userService.getUserByEmail(principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        return new ResponseEntity<>(userService.updateUserRest(user) , HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public HttpStatus deleteUser(@PathVariable Long id){
        userService.delete(id);
        return HttpStatus.OK;
    }

}