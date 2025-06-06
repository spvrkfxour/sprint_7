package ru.yandex.practicum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.steps.dto.CreateCourierRequest;
import ru.yandex.practicum.steps.dto.CreateCourierRequestWithoutParameters;


public class CreateCourierSteps {
    private final CourierSteps courierSteps = new CourierSteps();

    @Step("Create courier")
    public void createCourierTest(CreateCourierRequest courier) {
        Response response = courierSteps.createCourier(courier);
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Create courier")
    public Response createCourierResponseTest(CreateCourierRequest courier) {
        Response response = courierSteps.createCourier(courier);
        Allure.step("Response Body: " + response.getBody().asString());
        return response;
    }

    @Step("Create courier")
    public void createCourierWithoutParameterTest(CreateCourierRequestWithoutParameters courierNull) {
        courierSteps.createCourierWithoutParameters(courierNull);
    }

    @Step("Create courier")
    public Response createCourierWithoutParameterResponseTest(CreateCourierRequestWithoutParameters courierNull) {
        return courierSteps.createCourierWithoutParameters(courierNull);
    }
}
