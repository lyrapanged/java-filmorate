package ru.yandex.practicum.filmorate.dao.filmDao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.filmDao.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM MPA_RATING " +
                "ORDER BY ID_RATING";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa getMpaById(Integer mpaId) {
        String sql = "SELECT * FROM MPA_RATING WHERE ID_RATING = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), mpaId).stream()
                .findFirst().orElseThrow(() -> new NotFoundException("Mpa doesn't exist"));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id_rating");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }
}
