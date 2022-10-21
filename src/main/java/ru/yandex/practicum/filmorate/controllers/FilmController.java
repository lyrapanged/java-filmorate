package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {

    private final static LocalDate LOWER_DATE = LocalDate.of(1895, 12, 28);

    private final FilmService filmService;

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        validationFilm(film);
        filmService.addFilm(film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        validationFilm(film);
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping(value = "{id}")
    public Film getFilm(@Valid @PathVariable Integer id) {
        return filmService.getFilm(id);
    }

    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PutMapping(value = "{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") Integer filmId, @PathVariable Integer userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping(value = "{id}/like/{userId}")
    public void removeLike(@PathVariable(value = "id") Integer filmId, @PathVariable Integer userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping(value = "/popular")
    public Set<Film> getMostPopularMovies(@RequestParam(defaultValue = "10", required = false) @Positive Integer count) {
        return filmService.topFilms(count);
    }

    private void validationFilm(Film film) {
        if (film.getReleaseDate().isBefore(LOWER_DATE)) {
            log.error("Bad release date");
            throw new ValidationException("Release date - no earlier than December 28, 1895.");
        }
        log.info("Validation passed successfully.");
    }
}


