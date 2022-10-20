package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.ReleaseDataException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final static LocalDate LOWER_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idFilm = 0;

    @Override
    public Film addFilm(Film film) {
        validationFilm(film);
        film.setId(++idFilm);
        films.put(film.getId(), film);
        log.info("Film added!");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validationFilm(film);
        if (films.get(film.getId()) == null) {
            log.error("Bad id");
            throw new ValidationException("ID doesn't exist");
        }
        films.put(film.getId(), film);
        log.info("Film updated.");
        return null;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Request getFilms");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Integer id) {
        if (films.get(id) == null) {
            throw new IncorrectParameterException("Id must be grater then zero");
        }
        return films.get(id);
    }

    private void validationFilm(Film film) {
        if (film.getReleaseDate().isBefore(LOWER_DATE)) {
            log.error("Bad release date");
            throw new ReleaseDataException("Release date - no earlier than December 28, 1895.");
        }
        log.info("Validation passed successfully.");
    }
}
