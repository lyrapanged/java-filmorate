package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer IdUser = 0;

    @Override
    public User addUser(User user) {
        user.setId(++IdUser);
        users.put(user.getId(), user);
        log.info("User added.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) == null) {
            log.error("Bad id");
            throw new ValidationException("ID doesn't exist");
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
    public User getUser(Integer id) {
        return users.entrySet().stream()
                .filter(p -> p.getKey().equals(id))
                .findFirst()
                .map(p -> p.getValue())
                .orElseThrow(() -> new NotFoundException("This user id does not exist"));
    }
}
