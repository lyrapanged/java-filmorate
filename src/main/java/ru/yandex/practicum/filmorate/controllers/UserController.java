package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer IdUser = 0;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        validationUser(user);
        user.setId(++IdUser);
        users.put(user.getId(), user);
        log.info("User added.");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validationUser(user);
        if (users.get(user.getId()) == null) {
            log.error("Bad id");
            throw new ValidationException("ID doesn't exist");
        }
        users.put(user.getId(), user);
        log.info("User updated.");
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Request getAllUser.");
        return new ArrayList<>(users.values());
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
