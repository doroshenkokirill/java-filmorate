package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.storages.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.storages.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userRepository;
    private final FriendsStorage friendsRepository;

    public UserDto addFriend(int id, int friendId) {
        if (id == friendId) {
            throw new ValidationException("Вы не можете добавить самого себя в друзья.");
        }
        User user = friendsRepository.addFriend(id, friendId);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto deleteFriend(int id, int friendId) {
        if (id == friendId) {
            throw new ValidationException("Вы не можете удалить самого себя из друзей.");
        }
        User user = friendsRepository.deleteFriend(id, friendId);
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> getMutualFriends(int id, int otherId) {
        if (id == otherId) {
            throw new ValidationException("Вы не можете искать общих друзей с самим собой.");
        }
        List<User> userFriends = friendsRepository.getAllFriendsById(id);
        List<User> otherUserFriends = friendsRepository.getAllFriendsById(id);
        List<User> mutualFriends;

        if (userFriends != null && otherUserFriends != null) {
            List<Integer> otherUserFriendsId = friendsRepository.getAllFriendsById(otherId)
                    .stream()
                    .map(User::getId)
                    .toList();
            mutualFriends = userFriends
                    .stream()
                    .filter(user -> otherUserFriendsId.contains(user.getId()))
                    .toList();
        } else {
            throw new NotFoundException("У вас с данным пользователем нет общих друзей.");
        }
        return mutualFriends.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public List<UserDto> getAllFriendsById(int id) {
        List<User> friends = friendsRepository.getAllFriendsById(id);
        return friends.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto create(UserDto userDto) {
        if ((userDto.getName() == null) || (userDto.getName().isBlank())) {
            userDto.setName(userDto.getLogin());
        }
        User user = userRepository.create(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UserDto newUserDto) {
        if (newUserDto.getName() == null || newUserDto.getName().isBlank()) {
            newUserDto.setName(newUserDto.getLogin());
        }
        User user = userRepository.update(UserMapper.mapToUser(newUserDto));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto getUser(int id) {
        User user = userRepository.getUser(id);
        return UserMapper.mapToUserDto(user);
    }
}