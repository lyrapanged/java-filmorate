package ru.yandex.practicum.filmorate.model.film;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer duration;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("id_rating", mpa.getId());
        return values;
    }
}
