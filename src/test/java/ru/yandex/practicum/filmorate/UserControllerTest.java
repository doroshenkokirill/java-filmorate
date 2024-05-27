package ru.yandex.practicum.filmorate;

import lombok.Builder;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController userController = new UserController(new UserManager());

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
}