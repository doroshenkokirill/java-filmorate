package ru.yandex.practicum.filmorate.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FilmManager {

    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(@RequestBody Film film) {
        if (checkFilm(film)) {
            log.info("Описание не может быть пустым");
            throw new ValidationException("Описание не может быть пустым");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм " + film.getFilmName() + " добавлен");
        return film;
    }

    public Film update(@RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null || checkFilm(newFilm)) {
            log.info("Формат фильма неверен");
            throw new ValidationException("Формат фильма неверен");
        }
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setFilmName(newFilm.getFilmName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        log.info("Фильм " + oldFilm.getFilmName() + " обновлен");
        return oldFilm;
    }

    private boolean checkFilm(Film film) {
        if (film.getFilmName() == null || film.getFilmName().isEmpty()) {
            throw new ValidationException("Название не должно быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration().getSeconds() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        return false;
    }

    private long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}