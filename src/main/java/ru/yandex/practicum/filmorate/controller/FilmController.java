package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmManager filmManager;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmManager.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmManager.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return filmManager.update(newFilm);
    }
}