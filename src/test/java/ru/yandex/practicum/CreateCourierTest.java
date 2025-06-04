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


public class CreateCourierTest {
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
    @DisplayName("Create courier with all valid parameters")
    @Description("Success create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithValidParametersTest() {
        Response response = createCourierTest(login, password, firstName);
        return201Test(response);
        returnOkTrueBodyTest(response);
    }

    @Step("Create courier")
    public Response createCourierTest(String login, String password, String firstName) {
        Response response = courierSteps.createCourier(login, password, firstName, false);
        Allure.step("Response Body: " + response.getBody().asString());
        return response;
    }

    @Step("Return correct status code - 201")
    public void return201Test(Response response) {
        response.then().statusCode(201);
    }

    @Step("Create courier return correct body - { ok: true }")
    public void returnOkTrueBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Create courier with a login that is already taken")
    @Description("Failed create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Create another courier with same login")
    public void createCourierWithAlreadyCreatedLoginTest() {
        createCourierTest(login, password, firstName);
        Response response = createCourierTest(login,
                RandomStringUtils.randomAlphanumeric((int)(Math.random() * 8) + 3),
                RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + 3));
        return409Test(response);
        createCourierReturnAlreadyUseBodyTest(response);
    }

    @Step("Return correct status code - 409")
    public void return409Test(Response response) {
        response.then().statusCode(409);
    }

    @Step("Create courier return correct body - { \"message\": \"Этот логин уже используется\" }")
    public void createCourierReturnAlreadyUseBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(LOGIN_ALREADY_USE_ERROR));
    }

    @Test
    @DisplayName("Create courier with login = null")
    @Description("Failed create courier with login = null and random password and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithLoginNullTest() {
        Response response = createCourierTest(null, password, firstName);
        return400Test(response);
        createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Step("Return correct status code - 400")
    public void return400Test(Response response) {
        response.then().statusCode(400);
    }

    @Step("Create courier return correct body - { \"message\": \"Недостаточно данных для создания учетной записи\" }")
    public void createCourierReturnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(CREATE_COURIER_NOT_ENOUGH_DATA_ERROR));
    }

    @Test
    @DisplayName("Create courier with password = null")
    @Description("Failed create courier with password = null and random login and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithPasswordNullTest() {
        Response response = createCourierTest(login, null, firstName);
        return400Test(response);
        createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with firstName = null")
    @Description("Success create courier with firstName = null and random login and password parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithFirstNameNullTest() {
        Response response = createCourierTest(login, password, null);
        return201Test(response);
        returnOkTrueBodyTest(response);
    }

    @Test
    @DisplayName("Create courier without login")
    @Description("Failed create courier without login and random password and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutLoginTest() {
        Response response = createCourierWithoutParameterTest(null, password, firstName);
        return400Test(response);
        createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Step("Create courier")
    public Response createCourierWithoutParameterTest(String login, String password, String firstName) {
        return courierSteps.createCourier(login, password, firstName, true);
    }

    @Test
    @DisplayName("Create courier without password")
    @Description("Failed create courier without password and random login and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutPasswordTest() {
        Response response = createCourierWithoutParameterTest(login, null, firstName);
        return400Test(response);
        createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier without firstName")
    @Description("Success create courier without firstName and random login and password parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutFirstNameTest() {
        Response response = createCourierWithoutParameterTest(login, password, null);
        return201Test(response);
        returnOkTrueBodyTest(response);
    }

    @After
    public void tearDown() {
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        if (id != null) {
            courierSteps.deleteCourier(id);
        }
    }
}
