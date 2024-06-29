package ru.yandex.practicum.filmorate.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDto {
    @NotNull
    private int id;
    private String name;
}