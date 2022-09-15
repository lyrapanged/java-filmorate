package ru.yandex.practicum.filmorate.controllersTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
//@RequiredArgsConstructor
class UserControllerTest {
    private final User user = User.builder()
            .id(780)
            .email("@dada")
            .login("dadaya")
            .name("")
            .birthday(LocalDate.of(2020, 2, 22))
            .build();

    private UserController userController;

    @Autowired
    public UserControllerTest(UserController userController) {
        this.userController = userController;
    }

}