package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;


public class StatusCodeSteps {

    @Step("Return correct status code - 200")
    public void return200Test(Response response) {
        response.then().statusCode(200);
    }

    @Step("Return correct status code - 201")
    public void return201Test(Response response) {
        response.then().statusCode(201);
    }

    @Step("Return correct status code - 400")
    public void return400Test(Response response) {
        response.then().statusCode(400);
    }

    @Step("Return correct status code - 404")
    public void return404Test(Response response) {
        response.then().statusCode(404);
    }

    @Step("Return correct status code - 409")
    public void return409Test(Response response) {
        response.then().statusCode(409);
    }
}
