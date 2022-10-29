package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.userDao.FriendDao;
import ru.yandex.practicum.filmorate.dao.userDao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;
    private final FriendDao friendDao;

    @Autowired
    public UserService(@Qualifier("userDaoImpl") UserDao userDao, FriendDao friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    public void addUser(User user) {
        userDao.addUser(user);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUser(Integer id) {
        return userDao.getUser(id).orElseThrow(() -> new NotFoundException("User id doesn't exist"));
    }

    public void addFriend(Integer firstId, Integer secondId) {
        getUser(firstId);
        getUser(secondId);
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        friendDao.addFriend(firstId, secondId);
    }

    public void removeFriend(Integer firstId, Integer secondId) {
        getUser(firstId);
        getUser(secondId);
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        friendDao.removeFriend(firstId, secondId);

    }

    public List<User> getFriends(Integer id) {
        getUser(id);
        return new ArrayList<>(friendDao.getFriends(id));
    }

    public List<User> commonFriends(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot view mutual friends with yourself");
        }
        return friendDao.commonFriends(firstId, secondId);
    }
}
