package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_likes (ID_FILM, ID_USER) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE ID_FILM = ? AND ID_USER = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        String getPopularQuery = " SELECT * " +
                "from (SELECT FILMS.ID_FILM, NAME, DESCRIPTION, RELEASE_DATE, DURATION, ID_RATING " +
                "FROM films LEFT JOIN film_likes ON FILMS.ID_FILM = FILM_LIKES.ID_FILM " +
                "GROUP BY FILMS.ID_FILM ORDER BY COUNT(FILM_LIKES.ID_USER) DESC LIMIT ?) AS f " +
                "INNER JOIN (SELECT NAME AS MPA_NAME,ID_RATING AS ID_MPA FROM MPA_RATING)  AS MR  " +
                "ON f.ID_RATING = ID_MPA";
        return jdbcTemplate.query(getPopularQuery, (rs, rowNum) -> makeFilm(rs), count);
    }

    public List<Integer> getLikes(Integer filmId) {
        String sql = "SELECT ID_USER FROM film_likes WHERE ID_FILM = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id_user"), filmId);
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