package ru.yandex.practicum.filmorate.storage.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;

    public List<User> findAll() {
        String FIND_ALL = "SELECT * FROM users";
        return jdbc.query(FIND_ALL, mapper);
    }

    public User create(User user) {
        String CREATE = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
        int id = BaseDbStorage.insert(
                jdbc,
                CREATE,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday())
        );
        user.setId(id);
        return user;
    }

    public User update(User newUser) {
        String UPDATE = "UPDATE users " +
                "SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        int rows = jdbc.update(
                UPDATE,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                Date.valueOf(newUser.getBirthday()),
                newUser.getId()
        );
        if (rows == 0) {
            throw new NotFoundException("Такого пользователя нет");
        }
        return newUser;
    }

    public User getUser(int id) {
        String GET_USER = "SELECT * FROM users WHERE id = ?";
        return jdbc.queryForObject(GET_USER, mapper, id);
    }
}
