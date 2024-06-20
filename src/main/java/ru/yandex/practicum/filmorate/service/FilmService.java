package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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

    public void addLike(long filmId, long userId) {
        Film film = find(filmId);
        Set<Long> likes = film.getLikes();
        likes.add(userId);
        update(film);
        log.info("Пользователь {} лайкнул фильм {}", userStorage.find(userId), film.getName());
    }

    public void removeLike(long filmId, long userId) {
        if (userStorage.find(userId) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        Film film = find(filmId);
        film.getLikes().add(userId);
        update(film);
    }

    public Collection<Film> findTopRatedFilms(int size) {
        log.info("Список из топ-{}", size);
        return filmStorage.findAll().stream()
                .filter(film -> film.getLikes() != null)
                .filter(film -> film.getId() != null)
                .sorted((f1, f2) -> {
                    int likes1 = f1.getLikes().size();
                    int likes2 = f2.getLikes().size();
                    return likes2 - likes1;
                })
                .limit(size)
                .collect(Collectors.toList());
    }
}
