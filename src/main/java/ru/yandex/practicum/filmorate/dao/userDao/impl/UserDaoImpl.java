package ru.yandex.practicum.filmorate.dao.userDao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.userDao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("userDaoImpl")
@Slf4j
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id_user");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        log.info("Added user with id={}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        getUser(user.getId()).orElseThrow(() -> new NotFoundException("User does not exist"));
        String sqlQuery = "UPDATE users SET " +
                " email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id_user = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("User updated");
        return user;
    }

    @Override
    public Optional<User> getUser(Integer userId) {
        if (userId == null) {
            throw new ValidationException("Bad id");
        }
        String sql = "SELECT * FROM users WHERE ID_USER = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId).stream().findFirst();
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id_user");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}