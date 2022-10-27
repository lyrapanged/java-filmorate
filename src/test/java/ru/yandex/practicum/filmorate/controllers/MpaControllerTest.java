package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.film.MpaService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MpaControllerTest {
    private final MpaService mpaService;

    @Test
    void getAllMpa() {
        List<Mpa> mpa = new ArrayList<>(mpaService.getAllMpa());
        Mpa mpa1 = new Mpa(1, "G");
        Mpa mpa6 = new Mpa(5, "NC-17");
        assertEquals(mpa1, mpa.get(0));
        assertEquals(mpa6, mpa.get(4));
        assertEquals(5, mpa.size());
    }

    @Test
    void getMpaById() {
        List<Mpa> mpa = new ArrayList<>(mpaService.getAllMpa());
        Mpa mpa1 = new Mpa(2, "PG");
        Mpa mpa6 = new Mpa(3, "PG-13");
        assertEquals(mpa1, mpa.get(1));
        assertEquals(mpa6, mpa.get(2));
    }
}