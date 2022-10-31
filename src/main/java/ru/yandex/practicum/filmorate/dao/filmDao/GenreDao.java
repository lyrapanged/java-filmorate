package ru.yandex.practicum.filmorate.dao.filmDao;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getGenres();

    Genre getGenreById(Integer genreId);

    void delete(Film film);

    void add(Film film);

    List<Genre> getFilmGenres(Integer filmId);
}
