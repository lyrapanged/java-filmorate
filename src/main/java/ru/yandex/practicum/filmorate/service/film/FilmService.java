package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    //private final UserStorage userStorage;
    private final UserService userService;

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Film id doesn't exist"));
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(Integer idFilm, Integer idUser) {
        Film film = getFilm(idFilm);
        User user = userService.getUser(idUser);
        film.setFilmLikes(user.getId());
        film.setLikesCounter(film.getFilmLikes().size());
    }

    public void removeLike(Integer idFilm, Integer idUser) {
        Film film = getFilm(idFilm);
        User user = userService.getUser(idUser);
        film.removeLike(user.getId());
        film.setLikesCounter(film.getFilmLikes().size());
    }

    public Set<Film> topFilms(Integer count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesCounter)
                        .reversed())
                .limit(count).collect(Collectors.toSet());
    }
}
