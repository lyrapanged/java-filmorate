package ru.yandex.practicum.filmorate.model.film;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Mpa {
    private Integer id;
    @NotNull
    private String name;
}