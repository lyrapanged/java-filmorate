package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addUser(User user) {
        userStorage.addUser(user);
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(Integer id) {
        return userStorage.getUser(id).orElseThrow(() -> new NotFoundException("User id doesn't exist"));
    }

    public void addFriend(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        friendStorage.addFriend(firstId, secondId);
    }

    public void removeFriend(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        friendStorage.removeFriend(firstId, secondId);

    }

    public List<User> getFriends(Integer id) {
        return new ArrayList<>(friendStorage.getFriends(id));
    }

    public List<User> commonFriends(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot view mutual friends with yourself");
        }
        return friendStorage.commonFriends(firstId, secondId);
    }
}
