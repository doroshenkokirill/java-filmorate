package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.storages.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaRepository;

    public List<MpaDto> getAllMpa() {
        List<Mpa> mpa =  mpaRepository.getAllMpa();
        return mpa.stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }

    public MpaDto getMpa(int id) {
        Mpa mpa = mpaRepository.getMpa(id);
        return MpaMapper.mapToMpaDto(mpa);
    }
}
