package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
