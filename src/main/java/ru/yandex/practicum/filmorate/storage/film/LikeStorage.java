package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.film.GenreService;
import ru.yandex.practicum.filmorate.service.film.MpaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;


    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_likes (ID_FILM, ID_USER) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE ID_FILM = ? AND ID_USER = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        String getPopularQuery = "SELECT FILMS.ID_FILM, NAME, DESCRIPTION, RELEASE_DATE, DURATION, ID_RATING " +
                "FROM films LEFT JOIN film_likes ON FILMS.ID_FILM = FILM_LIKES.ID_FILM " +
                "GROUP BY FILMS.ID_FILM ORDER BY COUNT(FILM_LIKES.ID_USER) DESC LIMIT ?";

        return jdbcTemplate.query(getPopularQuery, (rs, rowNum) -> makeFilm(rs), count);
    }

    public List<Integer> getLikes(Integer filmId) {
        String sql = "SELECT ID_USER FROM film_likes WHERE ID_FILM = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id_user"), filmId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id_film");
        String email = rs.getString("name");
        String login = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_Date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Set<Integer> users = new HashSet<>(getLikes(rs.getInt("id_film")));
        Mpa mpa =  mpaService.getMpaById(rs.getInt("id_rating"));
        Set<Genre> genres = genreService.getFilmGenres(rs.getInt("id_film"));
        return new Film(id,email,login,releaseDate,duration,users,mpa,genres);
    }
}