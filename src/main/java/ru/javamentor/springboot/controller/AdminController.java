package ru.javamentor.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javamentor.springboot.model.User;
import ru.javamentor.springboot.service.RoleService;
import ru.javamentor.springboot.service.UserService;


import java.util.stream.Collectors;

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
    public String showAdminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userService.getUserByLogin(auth.getName()));
        model.addAttribute("users", userService.getAllUsers());
        return "admin_page";
    }

    @GetMapping("/add")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "add";
    }

    @PostMapping("/add")
    public String saveUser(@ModelAttribute("user") User user) {
        setUserRole(user);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/update")
    public String editUser(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "/update";
    }

    @PostMapping("/admin/{id}/update")
    public String updateUser(@ModelAttribute("user") User user) {
        setUserRole(user);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/{id}/delete")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    private void setUserRole(User user) {
        user.setRoles(user.getRoles().stream()
                .map(r -> roleService.getRole(r.getUserRole()))
                .collect(Collectors.toSet()));
    }
}