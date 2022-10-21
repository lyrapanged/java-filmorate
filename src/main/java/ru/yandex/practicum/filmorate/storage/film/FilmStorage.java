package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public List<Film> getFilms();

    public Optional<Film> getFilm(Integer id);
}
