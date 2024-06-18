package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        log.info("Вывод всех пользователей");
        return users.values();
    }

    @Override
    public User find(long id) {
        if (!users.containsKey(id)) {
            String message = "Пользователь c id" + id + " не найден";
            log.info("{}: {}", "input Error", message);
            throw new NotFoundException(message);
        }
        log.info("Пользователь с id {} найден", id);
        return users.get(id);
    }

    @Override
    public User create(User user) {
        checkUser(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getId() == null) {
            user.setId(getNextId());
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user.getName());
        return user;
    }

    @Override
    public User update(User newUser) {
        checkUser(newUser);
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getName() == null) {
                newUser.setName(newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            users.put(oldUser.getId(), oldUser);
            log.info("Пользователь {} обновлен", oldUser.getName());
            return oldUser;
        } else {
            throw new NotFoundException("Пользователь " + newUser.getName() + " не найден и не обновлен");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}