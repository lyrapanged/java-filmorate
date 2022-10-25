package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idUser = 0;

    @Override
    public User addUser(User user) {
        user.setId(++idUser);
        users.put(user.getId(), user);
        log.info("User added.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) == null) {
            log.error("Bad id");
            throw new NotFoundException("ID doesn't exist");
        }
        users.put(user.getId(), user);
        log.info("User updated.");
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Get all user.");
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUser(Integer id) {
        return Optional.ofNullable(users.get(id));
    }
}
