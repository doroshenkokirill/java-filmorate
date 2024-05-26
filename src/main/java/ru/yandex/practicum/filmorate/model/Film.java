package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;
    private String filmName;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
}
