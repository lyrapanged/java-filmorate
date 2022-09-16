package ru.yandex.practicum.filmorate.controllersTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    private final FilmController filmController = new FilmController();
    private final Film film = Film.builder()
            .id(521)
            .name("dada")
            .description("dadaya")
            .releaseDate(LocalDate.of(2020, 3, 3))
            .duration(120)
            .build();

    @Test
    void successfulValidation() {
        assertDoesNotThrow(() -> filmController.addFilm(film), "Ooops, something went wrong.");
        assertDoesNotThrow(() -> filmController.updateFilm(film), "Ooops, something went wring.");
    }

    @Test
    void validationTestNameIsBlank() {
        film.setName("     ");
        assertThrows(ValidationException.class, () -> filmController.addFilm(film), "Name is empty.");
    }

    @Test
    void validationTestMaxDescriptionLength() {
        film.setDescription("ale ale ale, ale ale ale, ale ale ale, ale ale ale, ale ale ale, ale ale ale, ale ale ale," +
                " ale ale ale, ale ale ale, ale ale ale, ale ale ale, ale ale ale, ale ale ale, ale ale ale," +
                " ale ale ale, ale ale");
        assertThrows(ValidationException.class, () -> filmController.addFilm(film),
                "Description is more than 200 symbols.");
    }

    @Test
    void validationTestReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895,12,27));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film), "Bad release day.");
    }

    @Test
    void validationTestDuration(){
        film.setDuration(-1);
        assertThrows(ValidationException.class,() -> filmController.addFilm(film), "Bad duration.");
    }

}