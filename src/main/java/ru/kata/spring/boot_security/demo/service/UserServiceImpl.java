package ru.kata.spring.boot_security.demo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
    public User save(User user){
       return userRepository.save(user);
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
    public User updateUserRest(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!existingUser.getUsername().equals(user.getUsername()) &&
                userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (!existingUser.getEmail().equals(user.getEmail()) &&
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        existingUser.setUsername(user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());
        Set<Role> validRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            String roleName = role.getName();
            System.out.println("Processing update role: " + roleName); // Debug
            Role dbRole = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            validRoles.add(dbRole);
        }
        existingUser.setRoles(validRoles);
        return userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public User createNewUserRest(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> validRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            String roleName = role.getName();
            System.out.println("Processing role: " + roleName); // Debug
            Role dbRole = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            validRoles.add(dbRole);
        }
        user.setRoles(validRoles);
        User savedUser = userRepository.save(user);
        System.out.println("Created user: " + savedUser); // Debug
        return savedUser;
    }
    public List<User>getAllUsersRest(){
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUserRest(Long id){
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        System.out.println("Deleted user ID: " + id); // Debug
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
