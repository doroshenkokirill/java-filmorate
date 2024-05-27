package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController filmController = new FilmController(new FilmManager());

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
}