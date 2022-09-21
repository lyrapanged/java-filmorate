package ru.yandex.practicum.filmorate.controllersTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private final User user = User.builder()
            .id(780)
            .email("@dada")
            .login("dadaya")
            .name("da")
            .birthday(LocalDate.of(2020, 2, 22))
            .build();

    private UserController userController = new UserController();

    @Test
    void successfulValidation() {
        assertDoesNotThrow(() -> userController.addUser(user), "Ooops");
        assertDoesNotThrow(() -> userController.updateUser(user), "Ooops");
    }

    @Test
    void validationTestEmailContainBadSymbols() {
        user.setEmail("dada");
        assertThrows(ValidationException.class, () -> userController.addUser(user), "Email contains \"@\"");
    }

    @Test
    void validationTestEmailIsBlank() {
        user.setEmail("        ");
        assertThrows(ValidationException.class, () -> userController.addUser(user), "Email is blank");
    }


    @Test
    void validationTestLoginIsBlank() {
        user.setLogin("    ");
        assertThrows(ValidationException.class, () -> userController.addUser(user), "Login is blank");
    }

    @Test
    void validationTestLoginContainBadSymbols() {
        user.setLogin("dada dada");
        assertThrows(ValidationException.class, () -> userController.addUser(user), "Login contain space");
    }

    @Test
    void validationTestBadBirthday() {
        user.setBirthday(LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.addUser(user), "Login contain space");
    }

}