package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film find(long id) {
        return filmStorage.find(id);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(long userId, long filmId) {
        Film film = find(filmId);
        film.getLikes().add(userId);
        update(film);
        log.info("Пользователь {} лайкнул фильм {}", userId, filmId);
        return film;
    }

    public Film removeLike(long userId, long filmId) {
        Film film = find(filmId);
        if (!film.getLikes().contains(userId)) {
            log.error("Пользователь {} не этот фильм {}", userId, filmId);
            throw new NotFoundException("User с userId:" + userId + "не лайкал фильм с filmId:" + filmId);
        }
        film.getLikes().remove(userId);
        update(film);
        return film;
    }

    public Collection<Film> findTopRatedFilms(long size) {
        Comparator<Film> comparator = Comparator.comparing(film -> film.getLikes().size());
        log.info("Возвращаем список из топ-{}", size);
        return findAll().stream().sorted(comparator.reversed()).limit(size).toList();
    }
}
