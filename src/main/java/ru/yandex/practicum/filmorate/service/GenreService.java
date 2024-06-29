package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.storages.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreRepository;

    public List<GenreDto> getAllGenres() {
        List<Genre> genres = genreRepository.getAllGenres();
        return genres.stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    public GenreDto getGenre(int id) {
        Genre genre = genreRepository.getGenre(id);
        return GenreMapper.mapToGenreDto(genre);
    }
}
