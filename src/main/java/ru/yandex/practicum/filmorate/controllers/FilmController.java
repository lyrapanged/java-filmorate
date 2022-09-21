package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final static int DESCRIPTION_LENGTH = 200;
    private final static LocalDate LOWER_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idFilm = 0;

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        validationFilm(film);
        film.setId(++idFilm);
        films.put(film.getId(), film);
        log.info("Film added!");
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        validationFilm(film);
        if (films.get(film.getId()) == null) {
            log.error("Bad id");
            throw new ValidationException("ID doesn't exist");
        }
        films.put(film.getId(), film);
        log.info("Film updated.");
        return film;
    }

    @GetMapping()
    public List<Film> getFilms() {
        log.info("Request getFilms");
        return new ArrayList<>(films.values());
    }

    private void validationFilm(Film film) {
        if (film.getName().isBlank()) {
            log.error("Bad name.");
            throw new ValidationException("Name cannot be empty.");
        }
        if (film.getDescription().length() > DESCRIPTION_LENGTH) {
            log.error("Bad description.");
            throw new ValidationException("Max description length is 200 symbols.");
        }
        if (film.getReleaseDate().isBefore(LOWER_DATE)) {
            log.error("Bad release date");
            throw new ValidationException("Release date - no earlier than December 28, 1895.");
        }
        if (film.getDuration() <= 0) {
            log.error("Bad duration.");
            throw new ValidationException("Film duration must be positive!");
        }
        log.info("Validation passed successfully.");
    }
}


