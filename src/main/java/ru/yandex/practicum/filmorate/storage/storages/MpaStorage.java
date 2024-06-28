package ru.yandex.practicum.filmorate.storage.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MpaStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<Mpa> mapperMpa;

    public List<Mpa> getAllMpa() {
        String getAllMpa = "SELECT * FROM mpa_rate";
        return jdbc.query(getAllMpa, mapperMpa);
    }

    public Mpa getMpa(int id) {
        check(id);
        String getMpa = "SELECT * FROM mpa_rate WHERE id = ?";
        return jdbc.queryForObject(getMpa, mapperMpa, id);
    }

    public void check(int id) throws NotFoundException {
        String check = "SELECT COUNT(id) FROM mpa_rate WHERE id = ?";
        Optional<Integer> countMpa = Optional.ofNullable(jdbc.queryForObject(check, Integer.class, id));
        if (countMpa.isEmpty()) {
            throw new InternalServerException("Ошибка проверки наличия рейтинга");
        } else if (countMpa.get() == 0) {
            throw new NotFoundException("Такого рейтинга нет");
        }
    }
}
