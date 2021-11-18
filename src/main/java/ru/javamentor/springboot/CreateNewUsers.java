package ru.javamentor.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.javamentor.springboot.model.Role;
import ru.javamentor.springboot.model.User;
import ru.javamentor.springboot.service.RoleService;
import ru.javamentor.springboot.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class CreateNewUsers {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public CreateNewUsers(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void newUsersInit() {
        Role roleAdmin = new Role("ADMIN");
        Role roleUser = new Role("USER");
        Set<Role> adminSet = new HashSet<>();
        Set<Role> userSet = new HashSet<>();

        roleService.addRole(roleAdmin);
        roleService.addRole(roleUser);

        adminSet.add(roleAdmin);
        userSet.add(roleUser);

        userService.saveUser(new User("UserName", "UserSurname", 13, "user1@gmail.com", "User",
                "user", userSet));
        userService.saveUser(new User("AdminName1", "AdminSurname", 24, "admin@gmail.com", "Admin",
                "admin", adminSet));
    }
}
