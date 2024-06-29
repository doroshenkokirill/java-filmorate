package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmLikeController {
    private final FilmService filmService;

    @GetMapping("/popular")
    public Collection<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int size) {
        log.info("getPopularFilms: count={}", size);
        return filmService.getPopularFilms(size);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("addLike: id={}, userId={}", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("deleteLike: id={}, userId={}", id, userId);
        return filmService.deleteLike(id, userId);
    }
}