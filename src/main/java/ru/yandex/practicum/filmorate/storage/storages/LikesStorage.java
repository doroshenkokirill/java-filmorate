package ru.yandex.practicum.filmorate.storage.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LikesStorage {

    private final JdbcTemplate jdbc;
    private final FilmStorage filmStorage;

    public List<Film> getPopularFilms(int count) {
        String getPopularFilms = "SELECT film_id " +
                "FROM films_like " +
                "GROUP BY film_id " +
                "ORDER BY COUNT(user_id) DESC " +
                "LIMIT ?";
        List<Integer> idPopularFilms = jdbc.queryForList(getPopularFilms, Integer.class, count);
        return idPopularFilms.stream()
                .map(filmStorage::getFilmById)
                .toList();
    }

    public Film addLike(int filmId, int userId) {
        String addLike = "SELECT COUNT(user_id) " +
                "FROM films_like " +
                "WHERE film_id = ? AND user_id = ?";
        Optional<Integer> count = Optional.ofNullable(jdbc.queryForObject(addLike, Integer.class, filmId, userId));
        if (count.isEmpty()) {
            throw new InternalServerException("Не удалось поставить лайк");
        }
        if (count.get() > 0) {
            throw new InternalServerException("Данный пользователь уже поставил лайк");
        }
        String addLike1 = "INSERT INTO films_like(film_id, user_id) " +
                "VALUES (?, ?)";
        int rows = jdbc.update(addLike1, filmId, userId);
        if (rows == 0) {
            throw new InternalServerException("Не удалось поставить лайк");
        }
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        String deleteLike = "DELETE FROM films_like WHERE user_id = ?";
        int rows = jdbc.update(deleteLike, userId);
        if (rows == 0) {
            throw new InternalServerException("Данный пользователь лайк не ставил");
        }
        return filmStorage.getFilmById(filmId);
    }
}
