package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;


@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreService genreService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       LikeStorage likeStorage, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreService = genreService;
    }

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
        genreService.putGenres(film);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
        genreService.putGenres(film);
    }

    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Film id doesn't exist"));
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.getFilm(filmId).orElseThrow(() -> new NotFoundException("Film or film doesn't exist"));
        userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("User or film doesn't exist"));
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("User or film doesn't exist"));
        filmStorage.getFilm(filmId).orElseThrow(() -> new NotFoundException("Film or film doesn't exist"));
        likeStorage.removeLike(filmId, userId);

    }

    public List<Film> getMostPopularMovies(Integer count) {
        return likeStorage.getPopularFilms(count);
    }

}
