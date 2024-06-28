package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storage.storages.GenreStorage;
import ru.yandex.practicum.filmorate.storage.storages.LikesStorage;
import ru.yandex.practicum.filmorate.storage.storages.MpaStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreRepository;
    private final LikesStorage likesRepository;

    public FilmDto addLike(int filmId, int userId) {
        Film film = likesRepository.addLike(filmId, userId);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto deleteLike(int filmId, int userId) {
        Film film = likesRepository.deleteLike(filmId, userId);
        return FilmMapper.mapToFilmDto(film);
    }

    public List<FilmDto> getPopularFilms(int count) {
        List<Film> popularFilms = likesRepository.getPopularFilms(count);
        return popularFilms.stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto create(FilmDto film) {
        check(film);
        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
                throw new ValidationException("Ошибка в дате выхода");
            }
        }

        Film cratedFilm = filmStorage.create(FilmMapper.mapToFilm(film));
        return FilmMapper.mapToFilmDto(cratedFilm);
    }

    private void check(FilmDto film) {
        try {
            if (film.getMpa() != null) {
                mpaStorage.check(film.getMpa().getId());
            }
            if (film.getGenres() != null) {
                film.getGenres().forEach(genre -> genreRepository.checkGenre(genre.getId()));
            }
        } catch (NotFoundException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public FilmDto update(FilmDto newFilm) {
        check(newFilm);
        if (newFilm.getReleaseDate() != null) {
            if (newFilm.getReleaseDate().isBefore(RELEASE_DATE)) {
                throw new ValidationException("Ошибка в дате релиза");
            }
        }
        Film film = filmStorage.update(FilmMapper.mapToFilm(newFilm));
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto getFilmById(int filmId) {
        Film film = filmStorage.getFilmById(filmId);
        return FilmMapper.mapToFilmDto(film);
    }

    public List<FilmDto> findAll() {
        return filmStorage.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}