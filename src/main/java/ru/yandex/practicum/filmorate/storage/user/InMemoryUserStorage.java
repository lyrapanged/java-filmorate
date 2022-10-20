package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.UserIdException;
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
        validationUser(user);
        user.setId(++IdUser);
        users.put(user.getId(), user);
        log.info("User added.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        validationUser(user);
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
        if (id <= 0) {
            throw new IncorrectParameterException("User id must be greater then zero");
        }
        if (!users.containsKey(id)) {
            throw new UserIdException("Id does not exist");
        }
        return users.get(id);
    }

    private void validationUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("Bad login");
            throw new ValidationException("Login cannot be empty and contain spaces.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Name has been set to Login");
            user.setName(user.getLogin());
        }
        log.info("Validation passed successfully");
    }
}
