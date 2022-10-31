package ru.yandex.practicum.filmorate.dao.userDao;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    public User addUser(User user);

    public User updateUser(User user);

    public List<User> getAllUsers();

    public Optional<User> getUser(Integer id);
}
