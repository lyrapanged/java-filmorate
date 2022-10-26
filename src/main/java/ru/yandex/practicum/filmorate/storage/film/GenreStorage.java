package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;

@Repository
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("id_genre"),
                rs.getString("name"))
        );
    }

    public Genre getGenreById(Integer genreId) {
        if (genreId == null) {
            throw new ValidationException("Bad id");
        }
        Genre genre;
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE ID_GENRE = ?", genreId);
        if (genreRows.first()) {
            genre = new Genre(
                    genreRows.getInt("id_genre"),
                    genreRows.getString("name")
            );
        } else {
            throw new NotFoundException("Genre does not exist");
        }
        return genre;
    }

    public void delete(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE ID_FILM = ?", film.getId());
    }

    public void add(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRE (ID_FILM, ID_GENRE) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
    }

    public List<Genre> getFilmGenres(Integer filmId) {
        String sql = "SELECT GENRES.ID_GENRE, NAME FROM FILM_GENRE" +
                " INNER JOIN GENRES ON FILM_GENRE.ID_GENRE = GENRES.ID_GENRE WHERE ID_FILM = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("id_genre"), rs.getString("name")), filmId
        );
    }
}