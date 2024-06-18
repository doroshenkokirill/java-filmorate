package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;


public interface FilmStorage {
    Collection<Film> findAll();

    Film find(long id);

    Film create(Film film);

    Film update(Film film);
}