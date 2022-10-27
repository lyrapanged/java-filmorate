package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
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

@Repository("filmDbStorage")
@Qualifier
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM FILMS";
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
        getFilm(film.getId()).orElseThrow(()-> new NotFoundException("Films doesn't exist"));
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
        String sql = "SELECT * FROM films WHERE ID_FILM = ?";
        return jdbcTemplate.query(sql,(rs, rowNum) -> makeFilm(rs),filmId).stream().findFirst();
    }

private Film makeFilm(ResultSet rs) throws SQLException {
    Integer idFilm = rs.getInt("id_film");
    String email = rs.getString("name");
    String login = rs.getString("description");
    LocalDate releaseDate = rs.getDate("release_Date").toLocalDate();
    Integer duration = rs.getInt("duration");
    String sqlNameMpa = "SELECT * FROM MPA_RATING WHERE ID_RATING IN (" +
            "SELECT ID_RATING FROM FILMS WHERE ID_FILM = ?)";
    String nameMpa = jdbcTemplate.query(sqlNameMpa,
                    (rsMpa,rowNum) -> rsMpa.getString("name"),idFilm).stream()
            .findAny().orElseThrow(()-> new NotFoundException("Bad name"));
    Mpa mpa = new Mpa(rs.getInt("id_rating"),nameMpa);
    String sqlGenres = "SELECT * FROM GENRES WHERE ID_GENRE IN " +
            "(SELECT ID_GENRE FROM FILM_GENRE WHERE FILM_GENRE.ID_FILM = ?)";
    Set<Genre> genres = new HashSet<>(
            jdbcTemplate.query(sqlGenres, (rsGenre, rowNum) -> makeGenre(rsGenre), idFilm));
    return new Film(idFilm,email,login,releaseDate,duration,mpa,genres);

}

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_genre");
        String name = rs.getString("name");
        return new Genre(id,name);
    }
}


