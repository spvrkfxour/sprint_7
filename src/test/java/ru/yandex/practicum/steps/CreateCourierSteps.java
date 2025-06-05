package ru.yandex.practicum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public class CreateCourierSteps {
    private final CourierSteps courierSteps = new CourierSteps();

    @Step("Create courier")
    public void createCourierTest(String login, String password, String firstName) {
        Response response = courierSteps.createCourier(login, password, firstName, false);
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Create courier")
    public Response createCourierResponseTest(String login, String password, String firstName) {
        Response response = courierSteps.createCourier(login, password, firstName, false);
        Allure.step("Response Body: " + response.getBody().asString());
        return response;
    }

    @Step("Create courier")
    public void createCourierWithoutParameterTest(String login, String password, String firstName) {
        courierSteps.createCourier(login, password, firstName, true);
    }

    @Step("Create courier")
    public Response createCourierWithoutParameterResponseTest(String login, String password, String firstName) {
        return courierSteps.createCourier(login, password, firstName, true);
    }
}
