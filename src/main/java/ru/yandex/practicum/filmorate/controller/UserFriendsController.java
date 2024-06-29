package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserFriendsController {
    private final UserService userService;

    @GetMapping("/{id}/friends")
    public List<UserDto> getAllFriends(@PathVariable int id) {
        log.info("Get all friends for user {}", id);
        return userService.getAllFriendsById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Get mutual friends for user {}", id);
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public UserDto addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Add friend for user {}", id);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public UserDto deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Delete friend for user {}", id);
        return userService.deleteFriend(id, friendId);
    }
}