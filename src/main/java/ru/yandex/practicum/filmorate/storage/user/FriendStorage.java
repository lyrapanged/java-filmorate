package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

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
        String sql = "SELECT ID_FRIEND, email, login, name, birthday FROM friends" +
                " INNER JOIN users ON friends.ID_FRIEND = users.ID_USER WHERE friends.ID_USER = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                        rs.getInt("id_friend"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate(),
                        null),
                userId
        );
    }
}
