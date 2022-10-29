package ru.yandex.practicum.filmorate.dao.userDao;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface FriendDao {
    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<User> getFriends(Integer userId);

    List<User> commonFriends(Integer firstId, Integer secondId);
}
