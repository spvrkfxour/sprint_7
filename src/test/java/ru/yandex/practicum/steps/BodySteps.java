package ru.yandex.practicum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.practicum.steps.env.EnvConf.*;


public class BodySteps {

    @Step("Return correct body - { ok: true }")
    public void returnOkTrueBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("ok", equalTo(true));
    }

    @Step("Login courier without parameters return correct body - { \"message\":  \"Недостаточно данных для входа\" }")
    public void loginCourierReturnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(LOGIN_COURIER_NOT_ENOUGH_DATA_ERROR));
    }

    @Step("Login courier with wrong parameters return correct body - { \"message\": \"Учетная запись не найдена\" }")
    public void loginCourierReturnNotFoundDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(LOGIN_DATA_NOT_FOUND_ERROR));
    }

    @Step("Get order return correct body - \"message\":  \"Недостаточно данных для поиска\"")
    public void returnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(GET_ORDER_TRACK_ID_NOT_ENOUGH_DATA_ERROR));
    }

    @Step("Get order return correct body - \"message\": \"Заказ не найден\"")
    public void returnOrderNotFoundBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(GET_ORDER_TRACK_ID_NOT_FOUND_ERROR));
    }

    @Step("Delete courier with nonexistent id return correct body - { \"message\": \"Курьера с таким id нет\" }")
    public void deleteCourierReturnNotFoundDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(DELETE_COURIER_NOT_FOUND_DATA_ERROR));
    }

    @Step("Delete courier without id return correct body - { \"message\":  \"Недостаточно данных для удаления курьера\" }")
    public void deleteCourierReturnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(DELETE_COURIER_NOT_ENOUGH_DATA_ERROR));
    }

    @Step("Create courier return correct body - { \"message\": \"Этот логин уже используется\" }")
    public void createCourierReturnAlreadyUseBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(LOGIN_ALREADY_USE_ERROR));
    }

    @Step("Create courier return correct body - { \"message\": \"Недостаточно данных для создания учетной записи\" }")
    public void createCourierReturnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(CREATE_COURIER_NOT_ENOUGH_DATA_ERROR));
    }

    @Step("Accept order return correct body - \"message\": \"Курьера с таким id не существует\"")
    public void returnCourierIdNotFoundBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(ACCEPT_COURIER_ID_NOT_FOUND_ERROR));
    }

    @Step("Accept order return correct body - \"message\": \"Заказа с таким id не существует\"")
    public void returnOrderIdNotFoundBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(ACCEPT_ORDER_ID_NOT_FOUND_ERROR));
    }
}
