package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

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
        User first = getUser(firstId);
        User second = getUser(secondId);
        first.setFriends(second.getId());
        second.setFriends(first.getId());
    }

    public void removeFriend(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        User first = getUser(firstId);
        User second = getUser(secondId);
        first.removeFriends(second.getId());
        second.removeFriends(first.getId());

    }

    public List<User> getFriends(Integer id) {
        return getUser(id).getFriends().stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List<User> commonFriends(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot view mutual friends with yourself");
        }
        User first = getUser(firstId);
        User second = getUser(secondId);
        return first.getFriends().stream()
                .filter(second.getFriends()::contains)
                .map(this::getUser)
                .collect(Collectors.toList());
    }
}
