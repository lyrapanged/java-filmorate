package ru.yandex.practicum.filmorate.dao.filmDao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.filmDao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES" +
                " Order By  ID_GENRE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        if (genreId == null) {
            throw new ValidationException("Bad id");
        }
        String sql = "SELECT * FROM GENRES WHERE ID_GENRE = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), genreId).stream()
                .findFirst().orElseThrow(() -> new NotFoundException("Bad id"));
    }

    @Override
    public void delete(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE ID_FILM = ?", film.getId());
    }

    @Override
    public void add(Film film) {
        List<Genre> foo = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate("INSERT INTO FILM_GENRE (ID_FILM, ID_GENRE) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, foo.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return foo.size();
            }
        });
    }

    @Override
    public List<Genre> getFilmGenres(Integer filmId) {
        String sql = "SELECT GENRES.ID_GENRE, NAME FROM FILM_GENRE" +
                " INNER JOIN GENRES ON FILM_GENRE.ID_GENRE = GENRES.ID_GENRE WHERE ID_FILM = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), filmId
        );
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id_genre");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}