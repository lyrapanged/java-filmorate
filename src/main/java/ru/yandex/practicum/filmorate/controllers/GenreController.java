package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> getGenres() {
        log.info("Get genres");
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        log.info("Get genre by {}", id);
        return genreService.getGenreById(id);
    }
}