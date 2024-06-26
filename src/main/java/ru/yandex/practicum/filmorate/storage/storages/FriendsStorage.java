package ru.yandex.practicum.filmorate.storage.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FriendsStorage {
    private final JdbcTemplate jdbc;
    private final UserStorage userRepository;

    public List<User> getAllFriendsById(int id) {
        String getAllFriendsById = "SELECT friend_id AS user_id " +
                "FROM friends_list " +
                "WHERE user_id = ? " +
                "UNION " +
                "SELECT user_id AS user_id " +
                "FROM friends_list " +
                "WHERE friend_id = ? AND confirmation = true";
        checkUsers(id);
        List<Integer> idFriends = jdbc.queryForList(getAllFriendsById, Integer.class, id, id);
        return idFriends
                .stream()
                .map(userRepository::getUser)
                .toList();
    }

    public User addFriend(int id, int friendId) {
        checkUsers(id);
        checkUsers(friendId);
        String addFriend = "SELECT COUNT(user_id) " +
                "FROM friends_list " +
                "WHERE user_id = ? AND friend_id = ?";
        Optional<Integer> count1 = Optional.ofNullable(jdbc.queryForObject(addFriend, Integer.class, id, friendId));
        Optional<Integer> count2 = Optional.ofNullable(jdbc.queryForObject(addFriend, Integer.class, friendId, id));
        if (count1.isEmpty() || count2.isEmpty()) throw new InternalServerException("Ошибка добавления в друзья");
        if (count1.get() > 0) throw new InternalServerException("Вы уже отправили заявку этому пользователю");
        else if (count2.get() > 0) {
            String addFriend1 = "UPDATE friends_list " +
                    "SET confirmation = true " +
                    "WHERE user_id = ? AND friend_id = ?";
            int rowsUpdate = jdbc.update(addFriend1, friendId, id);
            if (rowsUpdate == 0) throw new NotFoundException("Такого пользователя нет");
            return userRepository.getUser(id);
        }
        String addFriend2 = "INSERT INTO friends_list(user_id, friend_id, confirmation)" +
                "VALUES (?, ?, FALSE)";
        int rowsCreated = jdbc.update(addFriend2, id, friendId);
        if (rowsCreated == 0) throw new InternalServerException("Не удалось добавить пользователя в друзья");
        return userRepository.getUser(id);
    }

    public User deleteFriend(int userId, int friendId) {
        checkUsers(userId);
        checkUsers(friendId);
        String deleteFriend = "DELETE FROM friends_list WHERE user_id = ? AND friend_id = ?";
        jdbc.update(deleteFriend, userId, friendId);
        return userRepository.getUser(userId);
    }

    private void checkUsers(int id) {
        String checkUsers = "SELECT COUNT(id) FROM users WHERE id = ?";
        Optional<Integer> haveUser = Optional
                .ofNullable(jdbc.queryForObject(checkUsers, Integer.class, id));
        if (haveUser.isEmpty()) throw new InternalServerException("Ошибка добавления в друзья");
        else if (haveUser.get() == 0) throw new NotFoundException("Такого пользователя нет");
    }
}