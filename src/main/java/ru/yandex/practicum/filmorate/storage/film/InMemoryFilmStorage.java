package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;


@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idFilm = 0;

    @Override
    public Film addFilm(Film film) {
        film.setId(++idFilm);
        films.put(film.getId(), film);
        log.info("Film added!");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.get(film.getId()) == null) {
            log.error("Bad id");
            throw new NotFoundException("ID doesn't exist");
        }
        films.put(film.getId(), film);
        log.info("Film updated.");
        return null;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Request getFilms");
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        return Optional.ofNullable(films.get(id));
    }
}
