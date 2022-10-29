package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.filmDao.FilmDao;
import ru.yandex.practicum.filmorate.dao.filmDao.LikeDao;
import ru.yandex.practicum.filmorate.dao.userDao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmControllerTest {

    private final FilmDao filmDao;
    private final LikeDao likeDao;
    private final UserDao userDao;


    @Test
    public void test_addFilm() {
        Film film = Film.builder()
                .id(1)
                .name("dada")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(film);
        Assertions.assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
        assertEquals(1, filmDao.getFilms().size());
    }

    @Test
    public void test_updateFilm() {
        Film film = Film.builder()
                .id(1)
                .name("dada")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(film);
        Assertions.assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
        Film second = Film.builder()
                .id(1)
                .name("Ten little niggers")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.updateFilm(second);
        assertThat("Ten little niggers").isEqualTo(filmDao.getFilm(1).orElseThrow().getName());
    }

    @Test
    public void test_getFilm() {
        Film film = Film.builder()
                .id(1)
                .name("dada")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(film);
        assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
    }

    @Test
    public void test_getFilms() {
        Film film = Film.builder()
                .id(1)
                .name("dada")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(film);
        assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
        Film testFilm = filmDao.getFilm(1).orElseThrow(() -> new NotFoundException("s"));
        assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
        Film second = Film.builder()
                .id(1)
                .name("Ten little niggers")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(second);
        assertEquals(2, filmDao.getFilms().size(), "Bad size");

    }


    @Test
    public void test_addLike() {
        Film film = Film.builder()
                .id(1)
                .name("dada")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(film);
        assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
        Film testFilm = filmDao.getFilm(1).orElseThrow(() -> new NotFoundException("s"));
        assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
        Film second = Film.builder()
                .id(1)
                .name("Ten little niggers")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(second);
        assertEquals(2, filmDao.getFilms().size(), "Bad size");
        User user = User.builder()
                .id(1)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userDao.addUser(user);
        likeDao.addLike(1, 1);
        assertEquals(1, likeDao.getPopularFilms(1).stream().findFirst().orElseThrow().getId());
    }

    @Test
    public void test_removeLikeAndPopularFilm() {
        Film film = Film.builder()
                .id(1)
                .name("dada")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(film);
        assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
        Film testFilm = filmDao.getFilm(1).orElseThrow(() -> new NotFoundException("s"));
        assertThat(film).isEqualTo(filmDao.getFilm(1).orElseThrow());
        Film second = Film.builder()
                .id(1)
                .name("Ten little niggers")
                .description("dadaya")
                .releaseDate(LocalDate.of(2020, 3, 3))
                .duration(120)
                .genres(new HashSet<>())
                .mpa(new Mpa(1, "G"))
                .build();
        filmDao.addFilm(second);
        assertEquals(2, filmDao.getFilms().size(), "Bad size");
        User user = User.builder()
                .id(1)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        User secondUser = User.builder()
                .id(2)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        User third = User.builder()
                .id(3)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userDao.addUser(user);
        userDao.addUser(secondUser);
        userDao.addUser(third);
        likeDao.addLike(1, 1);
        assertEquals(1, likeDao.getPopularFilms(1).stream().findFirst().orElseThrow().getId());
        likeDao.addLike(1, 2);
        likeDao.addLike(2, 3);
        assertEquals(likeDao.getPopularFilms(1).stream().findFirst().orElseThrow(), film);
        likeDao.removeLike(1, 1);
        likeDao.removeLike(1, 2);
        assertEquals(likeDao.getPopularFilms(1).stream().findFirst().orElseThrow(), second);
    }

}