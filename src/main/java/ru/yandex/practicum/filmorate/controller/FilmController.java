package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new ValidationException("Описание не может быть пустым");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getDescription() == null || newFilm.getDescription().isBlank()) {
                throw new ValidationException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldFilm.setDescription(newFilm.getDescription());
            return oldFilm;
        }
        throw new NotFoundException("Пост с id = " + newFilm.getId() + " не найден");
    }
}