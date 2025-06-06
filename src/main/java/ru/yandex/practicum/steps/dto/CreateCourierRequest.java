package ru.yandex.practicum.steps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CreateCourierRequest {
    private String login;
    private String password;
    private String firstName;

    public CreateCourierRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
