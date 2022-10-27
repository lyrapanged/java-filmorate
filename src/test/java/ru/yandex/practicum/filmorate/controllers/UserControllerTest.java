package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {


    private final UserDbStorage userStorage;
    private final FriendStorage friendStorage;


    @Test
    void test_addUser() {
        User user = User.builder()
                .id(1)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(user);
        Assertions.assertThat(user).isEqualTo(userStorage.getUser(1).orElseThrow());
    }

    @Test
    void test_updateUser() {
        User user = User.builder()
                .id(1)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(user);
        user.setName("j");
        userStorage.updateUser(user);
        assertThat("j").isEqualTo(userStorage.getUser(1).orElseThrow().getName());
    }

    @Test
    void test_getAllUsers() {
        User user = User.builder()
                .id(1)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(user);
        User second = User.builder()
                .id(2)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(second);
        assertEquals(2, userStorage.getAllUsers().size(), "Bad size");
    }

    @Test
    void test_addFriend() {
        User user = User.builder()
                .id(1)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(user);
        User second = User.builder()
                .id(2)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(second);
        friendStorage.addFriend(user.getId(), second.getId());
        List<User> friends = friendStorage.getFriends(1);
        User testFriend = friends.stream().findFirst().orElseThrow();
        assertEquals(2, testFriend.getId());
    }

    @Test
    void test_removeFriend() {
        User user = User.builder()
                .id(1)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(user);
        User second = User.builder()
                .id(2)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(second);
        friendStorage.addFriend(user.getId(), second.getId());
        List<User> friends = friendStorage.getFriends(1);
        User testFriend = friends.stream().findFirst().orElseThrow();
        assertEquals(2, testFriend.getId());
        friendStorage.removeFriend(1, 2);
        assertEquals(0, friendStorage.getFriends(1).size());
    }

    @Test
    void test_commonFriend() {
        User user = User.builder()
                .id(1)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(user);
        User second = User.builder()
                .id(2)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        User third = User.builder()
                .id(3)
                .email("@dada")
                .login("dadaya")
                .name("asd")
                .birthday(LocalDate.of(2020, 2, 22))
                .build();
        userStorage.addUser(second);
        userStorage.addUser(third);
        friendStorage.addFriend(user.getId(), second.getId());
        List<User> friends = friendStorage.getFriends(1);
        assertEquals(2, friends.stream().findFirst().orElseThrow().getId());
        friendStorage.addFriend(user.getId(), third.getId());
        friendStorage.addFriend(second.getId(), third.getId());
        friendStorage.addFriend(second.getId(), user.getId());
        assertEquals(3, friendStorage.commonFriends(1, 2).stream().findFirst().orElseThrow().getId(), "404");
    }
}