package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;


public interface UserService {

    User createNewUserRest(UserDTO userDTO);
    List<User>getAllUsersRest();
    User updateUserRest(User user);
    void deleteUserRest(Long id);


    void createUser(String firstName, String lastName, int age ,
                    String email , String password , Set<Role> roles);
    List<User> listAll();
    User save(User user);
    User get(Long id);
    void delete(Long id);
    User getUserByEmail(String email);

    void updateUser( User user);
}
