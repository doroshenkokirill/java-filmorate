package ru.yandex.practicum.filmorate;

import lombok.Builder;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private final UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    public void createUserWithoutErrorsTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        assertDoesNotThrow(() -> userController.create(user));
    }

    @Test
    @Builder
    public void createUserWithEmailErrorTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email_sobaka_ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void createUserWithEmailErrorSecondTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void createUserWithBirthdayErrorTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2048, 2, 2));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    public void createUserWithLoginErrorTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User 1234");
        user.setBirthday(LocalDate.of(2048, 2, 2));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void createUserWithLoginErrorSecondTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
//        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2048, 2, 2));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void updateUserTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        user.setEmail("email123@ya.ru");
        userController.update(user);
    }

    @Test
    public void createUserWithoutNameTest() {
        User user = new User();
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        System.out.println(userController.getUsers());
    }

    @Test
    public void createUserWithoutIdTest() {
        User user = new User();
        user.setEmail("123@mail.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        System.out.println(userController.getUsers());
    }

    @Test
    public void addToFriendsListTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        User user1 = new User();
        user1.setName("User Friend");
        user1.setEmail("emailfr@ya.ru");
        user1.setLogin("Userfr1234");
        user1.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user1);
        userController.addFriend(user1.getId(), user.getId());
        assertEquals(user.getFriends().size(), 1);
        assertEquals(user1.getFriends().size(), 1);

    }

    @Test
    public void removeFromFriendsList() {
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        User user1 = new User();
        user1.setName("User Friend");
        user1.setEmail("emailfr@ya.ru");
        user1.setLogin("Userfr1234");
        user1.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user1);
        userController.addFriend(user1.getId(), user.getId());
        assertEquals(user.getFriends().size(), 1); // друзья есть
        assertEquals(user1.getFriends().size(), 1);
        userController.deleteFriend(user1.getId(), user.getId());
        assertEquals(user.getFriends().size(), 0); // друзей нет
        assertEquals(user1.getFriends().size(), 0);
    }

    @Test
    public void findFriendsByIdTest() {
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        User user1 = new User();
        user1.setName("User Friend");
        user1.setEmail("emailfr@ya.ru");
        user1.setLogin("Userfr1234");
        user1.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user1);
        userController.addFriend(user1.getId(), user.getId());
        System.out.println(userController.findFriends(user1.getId()));
    }
}
