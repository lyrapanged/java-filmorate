package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.filmDao.MpaDao;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.Collection;


@RequiredArgsConstructor
@Service
public class MpaService {
    private final MpaDao mpaDao;

    public Collection<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    public Mpa getMpaById(Integer id) {
        return mpaDao.getMpaById(id);
    }
}
