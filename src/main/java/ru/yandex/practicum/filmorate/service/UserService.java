package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

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

    public User addToFriendsList(long userId, long newFriendId) {
        User user = find(userId);
        User friendOfUser = find(newFriendId);
        user.getFriends().add(newFriendId);
        friendOfUser.getFriends().add(userId);
        update(user);
        update(friendOfUser);
        return user;
    }

    public User removeFromFriendsList(long userId, long friendId) {
        User user = find(userId);
        User friend = find(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        update(user);
        update(friend);
        return user;
    }

    public Collection<User> findFriendsById(long id) {
        Collection<User> users = findAll();
        return users.stream().filter(someUsers -> someUsers.getFriends().contains(id)).toList();
    }

    public Collection<User> findMutualFriends(long userId, long anotherUserId) {
        Collection<User> users = findAll();
        User user = find(userId);
        User anotherUser = find(anotherUserId);
        List<Long> friendsId = user.getFriends().stream().filter(anotherUser.getFriends()::contains).toList();
        return users.stream().filter(curUser -> friendsId.contains(curUser.getId())).toList();
    }

}
