package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Setter(AccessLevel.PUBLIC)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@Builder
public class Film {
    @ToString.Include
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    @Setter(AccessLevel.PACKAGE)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    @Max(5000)
    private int duration;
}
