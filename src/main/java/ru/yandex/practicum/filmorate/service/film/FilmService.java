package ru.yandex.practicum.filmorate.service.film;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Data
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    public void addLike(Integer idFilm, Integer idUser) {
        Film film = inMemoryFilmStorage.getFilm(idFilm);
        User user = inMemoryUserStorage.getUser(idUser);
        film.setFilmLikes(user.getId());
        film.setLikesCounter(film.getFilmLikes().size());
    }

    public void removeLike(Integer idFilm, Integer idUser) {
        Film film = inMemoryFilmStorage.getFilm(idFilm);
        User user = inMemoryUserStorage.getUser(idUser);
        film.removeLike(user.getId());
        film.setLikesCounter(film.getFilmLikes().size());
    }

    public Set<Film> topFilms(Integer count) {
        return inMemoryFilmStorage.getFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesCounter)
                        .reversed())
                .limit(count).collect(Collectors.toSet());
    }
}
