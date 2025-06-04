package ru.yandex.practicum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import ru.yandex.practicum.steps.CourierSteps;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.practicum.steps.env.EnvConf.*;


public class DeleteCourierTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        // Количество символов: MIN_COUNT_LENGTH + 7
        login = RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        password = RandomStringUtils.randomAlphanumeric((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        firstName = RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
    }

    @Test
    @DisplayName("Delete courier with valid id")
    @Description("Success delete courier with valid id in request. DELETE \"/api/v1/courier/{id}\"")
    public void deleteCourierTest() {
        createCourierTest(login, password, firstName);
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        Response response = courierSteps.deleteCourier(id);
        return200Test(response);
        returnOkTrueBodyTest(response);
    }

    @Step("Create courier")
    public void createCourierTest(String login, String password, String firstName) {
        Response response = courierSteps.createCourier(login, password, firstName, false);
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Return correct status code - 200")
    public void return200Test(Response response) {
        response.then().statusCode(200);
    }

    @Step("Create courier return correct body - { ok: true }")
    public void returnOkTrueBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Delete courier with nonexistent id")
    @Description("Failed delete courier with nonexistent id in request. DELETE \"/api/v1/courier/{id}\"")
    public void deleteCourierWithNonexistentIdTest() {
        createCourierTest(login, password, firstName);
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        Response response = courierSteps.deleteCourier(id + id);
        return404Test(response);
        deleteCourierReturnNotFoundDataBodyTest(response);
    }

    @Step("Return correct status code - 404")
    public void return404Test(Response response) {
        response.then().statusCode(404);
    }

    @Step("Delete courier with nonexistent id return correct body - { \"message\": \"Курьера с таким id нет\" }")
    public void deleteCourierReturnNotFoundDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(DELETE_COURIER_NOT_FOUND_DATA_ERROR));
    }

    @Test
    @DisplayName("Delete courier without id")
    @Description("Failed delete courier without id params in request. DELETE \"/api/v1/courier/{id}\"")
    public void deleteCourierWithoutIdTest() {
        createCourierTest(login, password, firstName);
        Response response = courierSteps.deleteCourierWithoutId();
        return400Test(response);
        deleteCourierReturnNotEnoughDataBodyTest(response);
    }

    @Step("Return correct status code - 400")
    public void return400Test(Response response) {
        response.then().statusCode(400);
    }

    @Step("Delete courier without id return correct body - { \"message\":  \"Недостаточно данных для удаления курьера\" }")
    public void deleteCourierReturnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(DELETE_COURIER_NOT_ENOUGH_DATA_ERROR));
    }

    @After
    public void tearDown() {
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        if (id != null) {
            courierSteps.deleteCourier(id);
        }
    }
}
