package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String viewHomePage(Model model, Principal principal) {
        List<User> users = userService.listAll();
        User admin = userService.getUserByEmail(principal.getName());
        model.addAttribute("users", users);
        model.addAttribute("email", admin.getEmail());
        model.addAttribute("role", admin.getAllRolesString());
        model.addAttribute("roles",roleService.getAllRoles());
        model.addAttribute("user", new User());
        model.addAttribute("userData", userService.getUserByEmail(principal.getName()));

        return "admin";
    }

    @GetMapping("/create")
    public String showNewUserPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "new_user";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user) {
        userService.save(user);

        return "redirect:/admin";
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id")Long id){
        userService.delete(id);
        return "redirect:/admin";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}
