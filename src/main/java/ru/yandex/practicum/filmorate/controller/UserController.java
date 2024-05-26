package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserManager userManager;

    @GetMapping
    public Collection<User> getUsers() {
        return userManager.findAll();
    }

    @PostMapping
    public User create(User user) {
        return userManager.createUser(user);
    }

    @PutMapping
    public User update(User newUser) {
        return userManager.updateUser(newUser);
    }
}