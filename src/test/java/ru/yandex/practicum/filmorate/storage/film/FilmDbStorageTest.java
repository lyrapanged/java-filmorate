package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;

/*    @BeforeEach
    void setUp() {

    }

    @Test
    void addFilm(Film film){


    }

    @ParameterizedTest
    void getFilms() {
    }



    @ParameterizedTest
    void updateFilm() {
    }

    @ParameterizedTest
    void getFilm() {
        Optional<Film> userOptional = filmDbStorage.getFilm(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }*/
}