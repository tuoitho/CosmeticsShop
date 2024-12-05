package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.service.impl.RoleService;
import com.cosmeticsellingwebsite.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String getUserList(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/user/listUser";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "admin/user/createUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "admin/User/createUser";
        }
        userService.save(user);
        return "redirect:/admin/user";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.findAll());
            return "admin/User/editUser";
        }
        return "redirect:/admin/user";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "admin/user/editUser";
        }
        user.setUserId(id); // Set the ID to ensure correct user is updated
        userService.save(user);
        return "redirect:/admin/user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/user";
    }
}
