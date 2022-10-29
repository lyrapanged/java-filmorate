package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.filmDao.FilmDao;
import ru.yandex.practicum.filmorate.dao.filmDao.GenreDao;
import ru.yandex.practicum.filmorate.dao.filmDao.LikeDao;
import ru.yandex.practicum.filmorate.dao.userDao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.HashSet;
import java.util.List;


@Service
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final LikeDao likeDao;
    private final GenreService genreService;
    private final GenreDao genreDao;

    @Autowired
    public FilmService(@Qualifier("filmDaoImpl") FilmDao filmDao,
                       @Qualifier("userDaoImpl") UserDao userDao,
                       LikeDao likeDao, GenreService genreService, GenreDao genreDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.likeDao = likeDao;
        this.genreService = genreService;
        this.genreDao = genreDao;
    }

    public void addFilm(Film film) {
        filmDao.addFilm(film);
        genreService.putGenres(film);
    }

    public void updateFilm(Film film) {
        filmDao.updateFilm(film);
        genreService.putGenres(film);
    }

    public Film getFilm(Integer id) {
        Film film = filmDao.getFilm(id).orElseThrow(() -> new NotFoundException("Film id doesn't exist"));
        film.setGenres(new HashSet<>(genreDao.getFilmGenres(id)));
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmDao.getFilms();
        films.forEach(f -> f.setGenres(new HashSet<>(genreDao.getFilmGenres(f.getId()))));
        return films;
    }

    public void addLike(Integer filmId, Integer userId) {
        getFilm(filmId);
        getUser(userId);
        likeDao.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        getUser(userId);
        getFilm(filmId);
        likeDao.removeLike(filmId, userId);
    }

    public List<Film> getMostPopularMovies(Integer count) {
        return likeDao.getPopularFilms(count);
    }

    private void getUser(Integer userId) {
        userDao.getUser(userId).orElseThrow(() -> new NotFoundException("User or film doesn't exist"));
    }
}
