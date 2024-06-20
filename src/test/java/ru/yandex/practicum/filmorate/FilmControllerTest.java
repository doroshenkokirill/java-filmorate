package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private final UserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private final FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), inMemoryUserStorage));
    private final UserController userController = new UserController(new UserService(inMemoryUserStorage));

    @Test
    public void createFilmWithoutErrorsTest() {
        Film film = new Film();
        film.setName("New Film");
        film.setDescription("New Film description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    public void createFilmWithDescriptionErrorTest() {
        Film film = new Film();
        film.setName("New Film");
        film.setDescription("Фильм “Интерстеллар” - это научно-фантастическая эпопея, " +
                "исследующая границы космоса и человеческого познания. " +
                "В центре сюжета группа исследователей, отправляющихся сквозь червоточину " +
                "в поисках нового дома для человечества, сталкиваясь с загадками времени и пространства. " +
                "Режиссёр Кристофер Нолан мастерски сочетает науку, драму и философские размышления, " +
                "создавая глубокий и многогранный фильм, заставляющий задуматься о смысле жизни и " +
                "месте человека во Вселенной."); // >200 символов
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    public void createFilmWithReleaseErrorTest() {
        Film film = new Film();
        film.setName("New Film");
        film.setDescription("New Film description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(24, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    public void createFilmWithDurationErrorTest() {
        Film film = new Film();
        film.setName("New Film");
        film.setDescription("New Film description");
        film.setDuration(-90);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    public void createFilmWithNameErrorTest() {
        Film film = new Film();
        film.setName("");
        film.setDescription("New Film description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Название не должно быть пустым", exception.getMessage());
    }

    @Test
    public void updateFilmTest() {
        Film film = new Film();
        film.setName("123");
        film.setDescription("New Film description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        filmController.create(film);
        film.setDuration(80);
        filmController.update(film);
        assertEquals(film.getDuration(), 80);
    }

    @Test
    public void addLikeTest() {
        Film film = new Film();
        film.setName("000");
        film.setDescription("New Film description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        filmController.create(film);
        System.out.println(film.getLikes()); // лайков нет
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        filmController.like(user.getId(), film.getId());
        assertEquals(film.getLikes().size(), 1);
    }

    @Test
    public void removeLikeTest() {
        Film film = new Film();
        film.setName("000");
        film.setDescription("New Film description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        filmController.create(film);
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        filmController.like(user.getId(), film.getId());
        assertEquals(film.getLikes().size(), 1); // лайк есть
        filmController.removeLike(user.getId(), film.getId());
    }

    @Test
    public void findTopRatedFilmsTest() {
        Film film = new Film();
        film.setName("111");
        film.setDescription("New Film description");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        filmController.create(film);
        Film film1 = new Film();
        film1.setName("222");
        film1.setDescription("New Film description");
        film1.setDuration(90);
        film1.setReleaseDate(LocalDate.of(2024, 1, 1));
        filmController.create(film1);
        Film film2 = new Film();
        film2.setName("333");
        film2.setDescription("New Film description");
        film2.setDuration(90);
        film2.setReleaseDate(LocalDate.of(2024, 1, 1));
        filmController.create(film2);
        User user = new User();
        user.setName("User");
        user.setEmail("email@ya.ru");
        user.setLogin("User1234");
        user.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user);
        filmController.like(film1.getId(), user.getId());
        filmController.like(film.getId(), user.getId());
        User user1 = new User();
        user1.setName("User");
        user1.setEmail("email@ya.ru");
        user1.setLogin("User1234");
        user1.setBirthday(LocalDate.of(2000, 2, 2));
        userController.create(user1);
        filmController.like(film1.getId(), user1.getId());
        System.out.println(filmController.findTopRatedFilms(10));
    }
}