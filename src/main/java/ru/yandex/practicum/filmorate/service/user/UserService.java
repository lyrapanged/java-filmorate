package ru.yandex.practicum.filmorate.service.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class UserService {

    private final UserStorage userStorage;

    public void addFriend(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        User first = userStorage.getUser(firstId);
        User second = userStorage.getUser(secondId);
        first.setFriends(second.getId());
        second.setFriends(first.getId());
    }

    public void removeFriend(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        User first = userStorage.getUser(firstId);
        User second = userStorage.getUser(secondId);
        first.removeFriends(second.getId());
        second.removeFriends(first.getId());

    }

    public List<User> getFriends(Integer id) {
        return userStorage.getUser(id).getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> commonFriends(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot view mutual friends with yourself");
        }
        User first = userStorage.getUser(firstId);
        User second = userStorage.getUser(secondId);
        return first.getFriends().stream()
                .filter(second.getFriends()::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
