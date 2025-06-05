package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;
import ru.yandex.practicum.steps.*;
import org.junit.Test;
import ru.yandex.practicum.steps.dto.CreateCourierRequest;
import ru.yandex.practicum.steps.dto.CreateCourierRequestWithoutParameters;

import static ru.yandex.practicum.steps.env.EnvConf.*;


public class LoginCourierTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final CreateCourierSteps createCourier = new CreateCourierSteps();
    private final LoginCourierSteps loginCourier = new LoginCourierSteps();
    private final BodySteps body = new BodySteps();
    private String login;
    private String password;
    private String firstName;
    private CreateCourierRequest courier;
    private CreateCourierRequestWithoutParameters courierNull;

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
        courier = new CreateCourierRequest(login, password, firstName);
        createCourier.createCourierTest(courier);
        Response response = loginCourier.loginCourierTest(login, password);
        statusCode.return200Test(response);
        loginCourier.loginCourierReturnNotNullBodyTest(response);
    }

    @Test
    @DisplayName("Login courier with login = null")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with created courier parameters and login = null. POST \"/api/v1/courier/login\"")
    public void loginCourierWithNullLoginTest() {
        courier = new CreateCourierRequest(login, password, firstName);
        createCourier.createCourierTest(courier);
        Response response = loginCourier.loginCourierTest(null, password);
        statusCode.return400Test(response);
        body.loginCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier with password = null")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with created courier parameters and password = null. POST \"/api/v1/courier/login\"")
    public void loginCourierWithNullPasswordTest() {
        courier = new CreateCourierRequest(login, password, firstName);
        createCourier.createCourierTest(courier);
        Response response = loginCourier.loginCourierTest(login, null);
        statusCode.return400Test(response);
        body.loginCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier without login")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with created courier password and without login. POST \"/api/v1/courier/login\"")
    public void loginCourierWithoutLoginTest() {
        courierNull = new CreateCourierRequestWithoutParameters(login, password, firstName);
        createCourier.createCourierWithoutParameterTest(courierNull);
        Response response = loginCourier.loginCourierWithoutParametersTest(null, password);
        statusCode.return400Test(response);
        body.loginCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier without password")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with created courier login and without password. POST \"/api/v1/courier/login\"")
    public void loginCourierWithoutPasswordTest() {
        courierNull = new CreateCourierRequestWithoutParameters(login, password, firstName);
        createCourier.createCourierWithoutParameterTest(null);
        Response response = loginCourier.loginCourierWithoutParametersTest(login, null);
        statusCode.return400Test(response);
        body.loginCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier with wrong login")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with wrong login. POST \"/api/v1/courier/login\"")
    public void loginCourierWithWrongLoginTest() {
        courier = new CreateCourierRequest(login, password, firstName);
        createCourier.createCourierTest(courier);
        Response response = loginCourier.loginCourierTest(login + "t", password);
        statusCode.return404Test(response);
        body.loginCourierReturnNotFoundDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier with wrong password")
    @Description("Create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Failed login courier with wrong password. POST \"/api/v1/courier/login\"")
    public void loginCourierWithWrongPasswordTest() {
        courier = new CreateCourierRequest(login, password, firstName);
        createCourier.createCourierTest(courier);
        Response response = loginCourier.loginCourierTest(login, password + "t");
        statusCode.return404Test(response);
        body.loginCourierReturnNotFoundDataBodyTest(response);
    }

    @Test
    @DisplayName("Login courier with nonexistent courier")
    @Description("Courier was not created. Failed login courier with nonexistent courier. POST \"/api/v1/courier/login\"")
    public void loginCourierWithNonexistentDataLoginTest() {
        Response response = loginCourier.loginCourierTest(login, password);
        statusCode.return404Test(response);
        body.loginCourierReturnNotFoundDataBodyTest(response);
    }

    @After
    public void tearDown() {
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        if (id != null) {
            courierSteps.deleteCourier(id);
        }
    }
}
