package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    @Max(5000)
    private int duration;
}
