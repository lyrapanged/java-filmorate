package ru.yandex.practicum.filmorate.dao.filmDao;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Optional<Film> getFilm(Integer id);
}
