package ru.yandex.practicum.steps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateCourierRequestWithoutParameters {
    private String login;
    private String password;
    private String firstName;

    public CreateCourierRequestWithoutParameters(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
