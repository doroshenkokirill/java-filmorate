package ru.yandex.practicum.filmorate.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserManager {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User createUser(@RequestBody User user) {
        if (!checkUser(user)) {
            log.info("Пользователь задан неверно");
            throw new ValidationException("Пользователь задан неверно");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getId() == null) {
            user.setId(getNextId());
        }
        users.put(user.getId(), user);
        log.info("Пользователь " + user.getName() + " добавлен");
        return user;
    }

    public User updateUser(@RequestBody User newUser) {
        if (!checkUser(newUser)) {
            log.info("Пользователь задан неверно");
            throw new ValidationException("Пользователь задан неверно");
        }
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        User oldUser = users.get(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.info("Пользователь " + oldUser.getName() + " обновлен");
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean checkUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }
}