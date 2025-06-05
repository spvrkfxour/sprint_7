package ru.yandex.practicum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.notNullValue;


public class LoginCourierSteps {
    private final CourierSteps courierSteps = new CourierSteps();

    @Step("Login courier")
    public Response loginCourierTest(String login, String password) {
        return courierSteps.loginCourier(login, password, false);
    }

    @Step("Login courier")
    public Response loginCourierWithoutParametersTest(String login, String password) {
        return courierSteps.loginCourier(login, password, true);
    }

    @Step("Login courier body return id")
    public void loginCourierReturnNotNullBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("id", notNullValue());
    }
}
