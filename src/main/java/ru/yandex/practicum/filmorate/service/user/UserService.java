package ru.yandex.practicum.filmorate.service.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public void addFriend(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        User first = inMemoryUserStorage.getUser(firstId);
        User second = inMemoryUserStorage.getUser(secondId);
        first.setFriends(second.getId());
        second.setFriends(first.getId());
    }

    public void removeFriend(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot remove yourself");
        }
        User first = inMemoryUserStorage.getUser(firstId);
        User second = inMemoryUserStorage.getUser(secondId);
        first.removeFriends(second.getId());
        second.removeFriends(first.getId());

    }

    public List<User> getFriends(Integer id) {
        List<User> userFriends = new ArrayList<>();
        for (Integer friend : inMemoryUserStorage.getUser(id).getFriends()) {
            userFriends.add(inMemoryUserStorage.getUser(friend));
        }
        return userFriends;
    }

    public List<User> commonFriends(Integer firstId, Integer secondId) {
        if (firstId.equals(secondId)) {
            throw new NotFoundException("You cannot view mutual friends with yourself");
        }
        User first = inMemoryUserStorage.getUser(firstId);
        User second = inMemoryUserStorage.getUser(secondId);
        List<Integer> foo = first.getFriends().stream()
                .filter(second.getFriends()::contains)
                .collect(Collectors.toList());
        List<User> bar = new ArrayList<>();
        for (Integer integer : foo) {
            bar.add(inMemoryUserStorage.getUser(integer));
        }
        return bar;
    }
}
