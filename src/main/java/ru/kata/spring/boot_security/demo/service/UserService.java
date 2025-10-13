package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;


public interface UserService {
    void createUser(String firstName, String lastName, int age ,
                    String email , String password , Set<Role> roles);
    List<User> listAll();
    void save(User user);
    User get(Long id);
    void delete(Long id);
    User getUserByEmail(String email);

    void updateUser(User user);
}
