package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Integer id;
    @NonNull
    @NotBlank
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
}
