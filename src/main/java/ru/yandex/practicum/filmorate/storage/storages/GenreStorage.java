package ru.yandex.practicum.filmorate.storage.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<Genre> mapper;

    public List<Genre> getAllGenres() {
        String GET_ALL_GENRES = "SELECT * FROM genre";
        return jdbc.query(GET_ALL_GENRES, mapper);
    }

    public Genre getGenre(int id) {
        checkGenre(id);
        String GET_GENRE = "SELECT * FROM genre WHERE id = ?";
        return jdbc.queryForObject(GET_GENRE, mapper, id);
    }

    public void checkGenre(int id) throws NotFoundException {
        String CHECK_GENRE = "SELECT COUNT(id) FROM genre WHERE id = ?";
        Optional<Integer> countGenre = Optional.ofNullable(jdbc.queryForObject(CHECK_GENRE, Integer.class, id));
        if (countGenre.isEmpty()) {
            throw new InternalServerException("Ошибка добавления в друзья");
        } else if (countGenre.get() == 0) {
            throw new NotFoundException("Такого жанра нет");
        }
    }

    public List<Genre> genresToList(String listId) {
        List<Genre> genres = new ArrayList<>();
        if (listId != null && !listId.isEmpty()) {
            String[] idGenres = listId.split(", ");
            for (String idGenre : idGenres) {
                genres.add(getGenre(Integer.parseInt(idGenre)));
            }
        }
        return genres;
    }
}