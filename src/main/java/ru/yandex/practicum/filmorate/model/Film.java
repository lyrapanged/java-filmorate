package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
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
    private int duration;
    private Set<Integer> filmLikes = new HashSet<>();
    private int likesCounter;

    public void setFilmLikes(Integer idUser) {
        filmLikes.add(idUser);
    }

    public void removeLike(Integer idUser) {
        filmLikes.remove(idUser);
    }


}
