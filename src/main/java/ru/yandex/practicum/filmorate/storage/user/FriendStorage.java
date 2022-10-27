package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Autowired
    public FriendStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("User id does not exist"));
        User friend = userStorage.getUser(friendId).orElseThrow(() -> new NotFoundException("Friend id does not exist"));
        boolean status = false;
        if (friend.getFriends() == null || friend.getFriends().contains(userId)) {
            status = true;
            String sql = "UPDATE friends SET ID_USER = ? AND ID_FRIEND = ? AND STATUS = ? " +
                    "WHERE ID_USER = ? AND ID_FRIEND = ?";
            jdbcTemplate.update(sql, friendId, userId, true, friendId, userId);
        }
        String sql = "INSERT INTO friends (ID_USER, ID_FRIEND, STATUS) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, status);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("User id does not exist"));
        User friend = userStorage.getUser(friendId).orElseThrow(() -> new NotFoundException("Friend id does not exist"));
        String sql = "DELETE FROM friends WHERE ID_USER = ? AND ID_FRIEND = ?";
        jdbcTemplate.update(sql, userId, friendId);
        if (friend.getFriends() == null || friend.getFriends().contains(userId)) {
            sql = "UPDATE friends SET ID_USER = ? AND ID_FRIEND = ? AND STATUS = ? " +
                    "WHERE ID_USER = ? AND ID_FRIEND = ?";
            jdbcTemplate.update(sql, friendId, userId, false, friendId, userId);
        }
    }

    public List<User> getFriends(Integer userId) {
        userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("User id does not exist"));
        String sql = "SELECT * FROM USERS WHERE ID_USER in " +
                "(SELECT FRIENDS.ID_FRIEND FROM FRIENDS WHERE FRIENDS.ID_USER = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);

    }

    public List<User> commonFriends(Integer firstId, Integer secondId) {
        String sql = "SELECT * FROM USERS WHERE ID_USER IN " +
                "(SELECT ID_FRIEND FROM FRIENDS WHERE ID_USER = ? and ID_FRIEND IN " +
                "(SELECT ID_FRIEND FROM FRIENDS WHERE ID_USER = ?))";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs)
                , firstId, secondId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id_user");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday, null);
    }
}
