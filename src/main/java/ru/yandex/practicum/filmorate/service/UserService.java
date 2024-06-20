package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage storage) {
        this.userStorage = storage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User find(long id) {
        return userStorage.find(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public void addToFriendsList(long userId, long newFriendId) {
        User user = find(userId);
        User newFriend = find(newFriendId);
        Set<Long> userFriendList = user.getFriends();
        Set<Long> newFriendFriendList = newFriend.getFriends();
        userFriendList.add(newFriendId);
        newFriendFriendList.add(userId);
        update(user);
        update(newFriend);
        log.info("Пользователи {} и {} теперь друзья", user.getName(), newFriend.getName());
    }

    public void removeFromFriendsList(long userId, long friendId) {
        User user = find(userId);
        User friend = find(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        update(user);
        update(friend);
        log.info("Пользователь {} удалил из друзей {}", user.getName(), friend.getName());
    }

    public Collection<User> findFriendsById(long id) {
        if (find(id) == null) {
            throw new NotFoundException("Такого id нет");
        }
        log.info("Список друзей для пользователя {}", id);
        return findAll().stream()
                .filter(user -> user.getFriends().contains(id)).toList();
    }

    public Collection<User> findMutualFriends(long userId, long anotherUserId) {
        Collection<User> users = findAll();
        User user = find(userId);
        User anotherUser = find(anotherUserId);
        List<Long> friendsId = user.getFriends().stream().filter(anotherUser.getFriends()::contains).toList();
        log.info("Список общих друзей для пользователей {} и {}", user.getName(), anotherUser.getName());
        return users.stream().filter(someUser -> friendsId.contains(someUser.getId())).toList();
    }
}