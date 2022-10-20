package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class FilmService {

    public void addLike(@NonNull User user, @NonNull Film film) {
        film.setFilmLikes(user.getId());
        film.setLikesCounter(film.getFilmLikes().size());
    }

    public void removeLike(@NonNull User user, @NonNull Film film) {
        film.removeLike(user.getId());
        film.setLikesCounter(film.getFilmLikes().size());
    }

    public Set<Film> topFilms(InMemoryFilmStorage inMemoryFilmStorage, Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("Count must be grater then zero.");
        }
        return inMemoryFilmStorage.getFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesCounter)
                        .reversed())
                .limit(count).collect(Collectors.toSet());
    }
}
