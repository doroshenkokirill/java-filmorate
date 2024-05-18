package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private Long id;
    private String filmName;
    private String description;
    private Instant releaseDate;
    private Duration duration;
}
