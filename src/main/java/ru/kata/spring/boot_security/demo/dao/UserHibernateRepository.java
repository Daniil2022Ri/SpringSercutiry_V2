package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserHibernateRepository {
     List<User> findAll();
     void save(User user);
     void saveRole(Role role);
     User findById(Long id);
     void deleteById(Long id);
     User getUserByEmail(String email);
}
