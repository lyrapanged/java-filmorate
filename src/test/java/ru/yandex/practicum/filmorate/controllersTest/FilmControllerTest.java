package ru.yandex.practicum.filmorate.controllersTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
//@RequiredArgsConstructor
class FilmControllerTest {

    //private final FilmController filmController = new FilmController();
    private final FilmController filmController;
    private final Film film = Film.builder()
            .id(521)
            .name("dada")
            .description("dadaya")
            .releaseDate(LocalDate.of(2020, 3, 3))
            .duration(120)
            .build();

    @Autowired
    public FilmControllerTest(FilmController filmController) {
        this.filmController = filmController;
    }

}