package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.addFilm(film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    @GetMapping(value = "{id}")
    public Film getFilm(@Valid @PathVariable Integer id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    @GetMapping()
    public List<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
    }

    @PutMapping(value = "{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(inMemoryUserStorage.getUser(userId), inMemoryFilmStorage.getFilm(id));
    }

    @DeleteMapping(value = "{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(inMemoryUserStorage.getUser(userId), inMemoryFilmStorage.getFilm(id));
    }

    @GetMapping(value = "/popular")
    public Set<Film> getMostPopularMovies(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.topFilms(inMemoryFilmStorage, count);
    }
}


