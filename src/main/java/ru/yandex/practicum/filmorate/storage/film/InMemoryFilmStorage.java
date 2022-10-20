package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
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
            throw new ValidationException("ID doesn't exist");
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
    public Film getFilm(Integer id) {
        return films.entrySet().stream()
                .filter(p -> p.getKey().equals(id))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new NotFoundException("id does not exist"));
    }
}
