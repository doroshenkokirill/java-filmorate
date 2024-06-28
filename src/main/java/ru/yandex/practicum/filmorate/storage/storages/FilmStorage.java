package ru.yandex.practicum.filmorate.storage.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.Date;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapper;

    public List<Film> findAll() {
        String findAll = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
                "m.name AS mpa_name " +
                "FROM films AS f " +
                "INNER JOIN mpa_rate as m ON f.mpa_id = m.id";
        return jdbc.query(findAll, mapper);
    }

    public Film create(Film film) {
        String create = "INSERT INTO films(name, description, releaseDate, duration, genre, mpa_id)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        int id = BaseDbStorage.insert(
                jdbc,
                create,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                convertGenresToString(film.getGenre()),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    public Film update(Film newFilm) {
        String updateSql = "UPDATE films " +
                "SET name = ?, description = ?, releaseDate = ?, duration = ?, genre = ?, mpa_id = ? " +
                "WHERE id = ?";
        int rowsUpdated = jdbc.update(
                updateSql,
                newFilm.getName(),
                newFilm.getDescription(),
                Date.valueOf(newFilm.getReleaseDate()),
                newFilm.getDuration(),
                convertGenresToString(newFilm.getGenre()),
                newFilm.getMpa().getId(),
                newFilm.getId());
        if (rowsUpdated == 0) throw new NotFoundException("Ошибка в запросе");
        return newFilm;
    }

    public Film getFilmById(int filmId) {
        String getFilmById = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.genre, m.id AS mpa_id, " +
                "m.name AS mpa_name " +
                "FROM films AS f " +
                "INNER JOIN mpa_rate as m ON f.mpa_id = m.id WHERE f.id = ?";
        return jdbc.queryForObject(getFilmById, mapper, filmId);
    }

    private String convertGenresToString(List<Genre> genres) {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        if (genres != null) {
            for (Genre genre : genres) {
                sb.append(prefix);
                prefix = ", ";
                sb.append(genre.getId());
            }
        }
        return sb.toString();
    }
}
