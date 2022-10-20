package ru.yandex.practicum.filmorate.service.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    public void addFriend(@NonNull User first, @NonNull User second) {
        if (first.getId().equals(second.getId())) {
            throw new UserIdException("You cannot add yourself");
        }
        first.setFriends(second.getId());
        second.setFriends(first.getId());
    }

    public void removeFriend(@NonNull User first, @NonNull User second) {
        if (first.getId().equals(second.getId())) {
            throw new UserIdException("You cannot remove yourself");
        }
        first.removeFriends(second.getId());
        second.removeFriends(first.getId());

    }

    public List<User> getFriends(Integer id, @NonNull InMemoryUserStorage inMemoryUserStorage) {
        List<User> userFriends = new ArrayList<>();
        for (Integer friend : inMemoryUserStorage.getUser(id).getFriends()) {
            userFriends.add(inMemoryUserStorage.getUser(friend));
        }
        return userFriends;
    }

    public List<User> commonFriends(@NonNull User first, @NonNull User second,
                                    InMemoryUserStorage inMemoryUserStorage) {
        if (first.getId().equals(second.getId())) {
            throw new UserIdException("You cannot view mutual friends with yourself");
        }
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
