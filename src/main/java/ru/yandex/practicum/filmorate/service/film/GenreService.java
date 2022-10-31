package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.filmDao.GenreDao;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public Collection<Genre> getGenres() {
        return genreDao.getGenres();
    }

    public Genre getGenreById(Integer id) {
        return genreDao.getGenreById(id);
    }

    public void putGenres(Film film) {
        genreDao.delete(film);
        genreDao.add(film);
    }

    public Set<Genre> getFilmGenres(Integer filmId) {
        return new HashSet<>(genreDao.getFilmGenres(filmId));
    }
}
