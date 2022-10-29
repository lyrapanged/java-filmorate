package ru.yandex.practicum.filmorate.dao.filmDao;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.List;

public interface MpaDao {
    List<Mpa> getAllMpa();

    Mpa getMpaById(Integer mpaId);
}
