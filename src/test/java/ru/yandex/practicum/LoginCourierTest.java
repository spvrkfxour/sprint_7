package ru.yandex.practicum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;
import ru.yandex.practicum.steps.CourierSteps;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static ru.yandex.practicum.steps.env.EnvConf.*;


public class LoginCourierTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        // Количество символов: MIN_COUNT_LENGTH + 7
        login = RandomStringUtils.randomAlphabetic((int) (Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        password = RandomStringUtils.randomAlphanumeric((int) (Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        firstName = RandomStringUtils.randomAlphabetic((int) (Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
    }

    @Rule
    public Timeout globalTimeout = Timeout.millis(TIMEOUT_MILLISECONDS); // Для тестов с 504 ошибкой

    @Test
    @DisplayName("Login courier")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Success login courier with created courier parameters. POST \"/api/v1/courier/login\"")
    public void loginCourierTest() {
        createCourierTest(login, password, firstName);
        Response response = loginCourierTest(login, password);
        return200Test(response);
        loginCourierReturnNotNullBodyTest(response);
    }

    @Step("Create courier")
    public void createCourierTest(String login, String password, String firstName) {
        Response response = courierSteps.createCourier(login, password, firstName, false);
        Allure.step("Response Body: " + response.getBody().asString());
    }

    @Step("Login courier")
    public Response loginCourierTest(String login, String password) {
        return courierSteps.loginCourier(login, password, false);
    }

    @Step("Login courier")
    public Response loginCourierWithoutParametersTest(String login, String password) {
        return courierSteps.loginCourier(login, password, true);
    }

    @Step("Return correct status code - 200")
    public void return200Test(Response response) {
        response.then().statusCode(200);
    }

    @Step("Create courier body return id")
    public void loginCourierReturnNotNullBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("id", notNullValue());
    }

    @Test
    @DisplayName("Login courier with login = null")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with created courier parameters and login = null. POST \"/api/v1/courier/login\"")
    public void loginCourierWithNullLoginTest() {
        createCourierTest(login, password, firstName);
        Response response = loginCourierTest(null, password);
        return400Test(response);
        loginCourierReturnNotEnoughDataBodyTest(response);
    }

    @Step("Return correct status code - 400")
    public void return400Test(Response response) {
        response.then().statusCode(400);
    }

    @Step("Login courier without parameters return correct body - { \"message\":  \"Недостаточно данных для входа\" }")
    public void loginCourierReturnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(LOGIN_COURIER_NOT_ENOUGH_DATA_ERROR));
    }

    @Test
    @DisplayName("Login courier with password = null")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with created courier parameters and password = null. POST \"/api/v1/courier/login\"")
    public void loginCourierWithNullPasswordTest() {
        createCourierTest(login, password, firstName);
        Response response = loginCourierTest(login, null);
        return400Test(response);
        loginCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier without login")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with created courier password and without login. POST \"/api/v1/courier/login\"")
    public void loginCourierWithoutLoginTest() {
        createCourierWithoutParameterTest(login, password, firstName);
        Response response = loginCourierWithoutParametersTest(null, password);
        return400Test(response);
        loginCourierReturnNotEnoughDataBodyTest(response);
    }

    @Step("Create courier")
    public void createCourierWithoutParameterTest(String login, String password, String firstName) {
        courierSteps.createCourier(login, password, firstName, true);
    }

    @Test
    @DisplayName("Login courier without password")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with created courier login and without password. POST \"/api/v1/courier/login\"")
    public void loginCourierWithoutPasswordTest() {
        createCourierWithoutParameterTest(login, password, firstName);
        Response response = loginCourierWithoutParametersTest(login, null);
        return400Test(response);
        loginCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier with wrong login")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with wrong login. POST \"/api/v1/courier/login\"")
    public void loginCourierWithWrongLoginTest() {
        createCourierTest(login, password, firstName);
        Response response = loginCourierTest(login + "t", password);
        return404Test(response);
        loginCourierReturnNotFoundDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier with wrong password")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with wrong password. POST \"/api/v1/courier/login\"")
    public void loginCourierWithWrongPasswordTest() {
        createCourierTest(login, password, firstName);
        Response response = loginCourierTest(login, password + "t");
        return404Test(response);
        loginCourierReturnNotFoundDataBodyTest(response);
    }

    @Step("Return correct status code - 404")
    public void return404Test(Response response) {
        response.then().statusCode(404);
    }

    @Step("Login courier with wrong parameters return correct body - { \"message\": \"Учетная запись не найдена\" }")
    public void loginCourierReturnNotFoundDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(LOGIN_DATA_NOT_FOUND_ERROR));
    }

    @Test
    @DisplayName("Login courier with nonexistent courier")
    @Description("Courier was not created. Failed login courier with nonexistent courier. POST \"/api/v1/courier/login\"")
    public void loginCourierWithNonexistentDataLoginTest() {
        Response response = loginCourierTest(login, password);
        return404Test(response);
        loginCourierReturnNotFoundDataBodyTest(response);
    }

    @After
    public void tearDown() {
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        if (id != null) {
            courierSteps.deleteCourier(id);
        }
    }
}
