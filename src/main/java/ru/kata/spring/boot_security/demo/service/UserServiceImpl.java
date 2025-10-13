package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository ,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    private void construct(){
        Set<Role> roleAdmin = new HashSet<>();
        Role role = new Role("ROLE_ADMIN");
        roleAdmin.add(role);
        this.save(new User( "admin", "admin", 25, "$2a$10$aArNfgCqkpfW8SSCcA6E..A56VW//4ijWAVh4o78mkJM.6yDwNPtm", "admin@mail.com", roleAdmin));

        Set<Role> roleUser = new HashSet<>();
        Role roleU = new Role("ROLE_USER");
        roleUser.add(roleU);
        this.save(new User( "Daniil", "Rybiakov", 25, "$2a$10$mRFRuhocPANBW3nGPh02zeHZYWuILHwJze5lRcYVFXctE.G2lZKlm", "user@mail.com", roleUser));
    }
    @Transactional
    @Override
    public void createUser( String firstName, String lastName, int age ,
                            String email , String password , Set<Role> roles) {
        User user = new User();
        user.setUsername(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);
    }
    @Transactional
    @Override
    public List<User> listAll(){
        return userRepository.findAll().stream().distinct().collect(Collectors.toList());
    }
    @Transactional
    @Override
    public void save(User user){
        userRepository.save(user);
    }

    @Transactional
    @Override
    public User get(Long id){
        return userRepository.getById(id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));

        existingUser.setUsername(user.getUsername());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());
        existingUser.setRoles(user.getRoles());

        if (!user.getPassword().isEmpty() && !user.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existingUser);
    }

}
