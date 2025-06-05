package ru.yandex.practicum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;


public class GetCourierSteps {
    private final CourierSteps courierSteps = new CourierSteps();

    @Step("Get courier id")
    public Integer getCourierIdTest(String login, String password) {
        Allure.step("Courier id: " + courierSteps.loginCourier(login, password, false).then().extract().path("id"));
        return courierSteps.loginCourier(login, password, false).then().extract().path("id");
    }
}
