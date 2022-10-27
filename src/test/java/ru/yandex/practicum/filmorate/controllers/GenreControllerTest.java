package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreControllerTest {
    private final GenreService genreService;

    @Test
    void getGenres() {
        List<Genre> genres = new ArrayList<>(genreService.getGenres());
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(5, "Документальный");
        assertEquals(genre1, genres.get(0));
        assertEquals(genre2, genres.get(4));
        assertEquals(6, genres.size());
    }

    @Test
    void getGenreById() {
        List<Genre> genres = new ArrayList<>(genreService.getGenres());
        Genre genre1 = new Genre(2, "Драма");
        Genre genre2 = new Genre(4, "Триллер");
        assertEquals(genre1, genres.get(1));
        assertEquals(genre2, genres.get(3));
    }
}