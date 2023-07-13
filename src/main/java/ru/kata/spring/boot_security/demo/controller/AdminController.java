package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, @Lazy RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/all-users";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }

//    @PostMapping
//    public String create(@ModelAttribute("user") User user) {
//        userService.save(user);
//        return "redirect:/admin";
//    }


    @PostMapping
    public String create(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         @RequestParam("roles") List<String> roles) {
        User user = new User();
        List<Role> rolesToBeAdded = new ArrayList<>();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        Role role2 = null;
        Role role = roleRepository.findByName(roles.get(0)).orElse(null);
        if (roles.size() > 1) {
           role2 = roleRepository.findByName(roles.get(1)).orElse(null);
        }
        if (role != null && role2 != null) {
            rolesToBeAdded.add(role);
            rolesToBeAdded.add(role2);
        } else if (role != null) {
            rolesToBeAdded.add(role);
        } else {
            rolesToBeAdded.add(role2);
        }
        user.setRoles(rolesToBeAdded);
        userService.save(user);
        return "redirect:/admin";
    }
//    @PostMapping
//    public String create(@RequestParam("username") String username,
//                         @RequestParam("password") String password,
//                         @RequestParam("email") String email,
//                         @RequestParam("roles") String roles) {
//        User user = new User();
//        Role role = roleRepository.findByName(roles).orElse(null);
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setEmail(email);
//        if (role != null) {
//            user.setRoles(new HashSet<>(Set.of(role)));
//        }
//
//        userService.save(user);
//        return "redirect:/admin";
//    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id,
                       Model model) {
        model.addAttribute("user", userService.findOne(id));
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User updatedUser,
                         @PathVariable("id") long id) {
        System.out.println("test");
        userService.update(id, updatedUser);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
//@PostMapping
//    public String create(@RequestParam("username") String username,
//                         @RequestParam("password") String password,
//                         @RequestParam("email") String email,
//                         @RequestParam("roles") Set<Role> roles) {
//        User user = new User();
//        Role role_admin = new Role("ROLE_ADMIN");
//        Role role_user = new Role("ROLE_USER");
//        Set<Role> rolesToBeAdded = new HashSet<>();
//
//        Role roleAdminToBeAdded = roleRepository.findByName(roles
//                        .stream()
//                        .filter(foundRole -> Objects.equals(foundRole, role_admin))
//                        .findFirst()
//                        .get()
//                        .getName())
//                .orElse(null);
//        Role roleUserToBeAdded = roleRepository.findByName(roles
//                        .stream()
//                        .filter(foundRole -> Objects.equals(foundRole, role_user))
//                        .findFirst()
//                        .get()
//                        .getName())
//                .orElse(null);
//
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setEmail(email);
//
//        if (roleAdminToBeAdded != null) {
//            rolesToBeAdded.add(role_admin);
//        }
//
//        if (roleUserToBeAdded != null) {
//            rolesToBeAdded.add(role_user);
//        }
//        user.setRoles(rolesToBeAdded);
//
//        userService.save(user);
//        return "redirect:/admin";
//    }