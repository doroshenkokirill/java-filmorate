package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mpa {
    @Positive
    private int id;
    private String name;
}
