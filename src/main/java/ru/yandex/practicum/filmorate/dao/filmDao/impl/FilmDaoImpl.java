package ru.yandex.practicum.filmorate.dao.filmDao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.filmDao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository("filmDaoImpl")
@Qualifier
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM FILMS INNER JOIN " +
                "(SELECT NAME AS MPA_NAME,ID_RATING AS ID_MPA FROM MPA_RATING)  AS MR  ON FILMS.ID_RATING = ID_MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id_film");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        film.setMpa(film.getMpa());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Film doesn't exist");
        }
        getFilm(film.getId()).orElseThrow(() -> new NotFoundException("Films doesn't exist"));
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "ID_RATING = ? WHERE ID_FILM = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) != 0) {
            if (film.getGenres() != null) {
                Collection<Genre> sortGenres = film.getGenres().stream()
                        .sorted(Comparator.comparing(Genre::getId))
                        .collect(Collectors.toList());
                film.setGenres(new LinkedHashSet<>(sortGenres));
            }
            return film;
        } else {
            throw new NotFoundException("Film doesn't exist");
        }
    }

    @Override
    public Optional<Film> getFilm(Integer filmId) {
        if (filmId == null) {
            throw new NotFoundException("Id doesn't exist");
        }
        String sql = "SELECT * FROM FILMS INNER JOIN " +
                "(SELECT NAME AS MPA_NAME,ID_RATING AS ID_MPA FROM MPA_RATING)  AS MR  ON FILMS.ID_RATING = ID_MPA " +
                "WHERE ID_FILM =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), filmId).stream().findFirst();
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer idFilm = rs.getInt("id_film");
        String email = rs.getString("name");
        String login = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_Date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Mpa mpa = new Mpa(rs.getInt("id_rating"), rs.getString("mpa_name"));
        return new Film(idFilm, email, login, releaseDate, duration, mpa);
    }
}


