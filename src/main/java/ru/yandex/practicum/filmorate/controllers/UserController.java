package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        inMemoryUserStorage.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        inMemoryUserStorage.updateUser(user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @GetMapping(value = "{id}")
    public User getUser(@PathVariable Integer id) {
        return inMemoryUserStorage.getUser(id);
    }

    @PutMapping(value = "{id}/friends/{friendId}")
    public void addFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(inMemoryUserStorage.getUser(id), inMemoryUserStorage.getUser(friendId));

    }

    @DeleteMapping(value = "{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFriend(inMemoryUserStorage.getUser(id), inMemoryUserStorage.getUser(friendId));
    }

    @GetMapping(value = "{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        return userService.getFriends(id, inMemoryUserStorage);
    }

    @GetMapping(value = "{id}/friends/common/{otherId}")//
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.commonFriends(
                inMemoryUserStorage.getUser(id),inMemoryUserStorage.getUser(otherId), inMemoryUserStorage);
    }
}
