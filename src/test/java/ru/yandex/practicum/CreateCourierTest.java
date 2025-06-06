package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import ru.yandex.practicum.steps.BodySteps;
import ru.yandex.practicum.steps.CourierSteps;
import org.junit.Test;
import ru.yandex.practicum.steps.CreateCourierSteps;
import ru.yandex.practicum.steps.StatusCodeSteps;
import ru.yandex.practicum.steps.dto.CreateCourierRequest;
import ru.yandex.practicum.steps.dto.CreateCourierRequestWithoutParameters;

import static ru.yandex.practicum.steps.env.EnvConf.*;


public class CreateCourierTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final CreateCourierSteps createCourier = new CreateCourierSteps();
    private final BodySteps body = new BodySteps();
    private String login;
    private String password;
    private String firstName;
    private CreateCourierRequest courier;
    private CreateCourierRequestWithoutParameters courierNull;

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
        courier = new CreateCourierRequest(login, password, firstName);
        Response response = createCourier.createCourierResponseTest(courier);
        statusCode.return201Test(response);
        body.returnOkTrueBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with a login that is already taken")
    @Description("Failed create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Create another courier with same login")
    public void createCourierWithAlreadyCreatedLoginTest() {
        courier = new CreateCourierRequest(login, password, firstName);
        createCourier.createCourierResponseTest(courier);
        courier = new CreateCourierRequest(login,
                RandomStringUtils.randomAlphanumeric((int)(Math.random() * 8) + 3),
                RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + 3));
        Response response = createCourier.createCourierResponseTest(courier);
        statusCode.return409Test(response);
        body.createCourierReturnAlreadyUseBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with login = null")
    @Description("Failed create courier with login = null and random password and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithLoginNullTest() {
        courier = new CreateCourierRequest(null, password, firstName);
        Response response = createCourier.createCourierResponseTest(courier);
        statusCode.return400Test(response);
        body.createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with password = null")
    @Description("Failed create courier with password = null and random login and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithPasswordNullTest() {
        courier = new CreateCourierRequest(login, null, firstName);
        Response response = createCourier.createCourierResponseTest(courier);
        statusCode.return400Test(response);
        body.createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with firstName = null")
    @Description("Success create courier with firstName = null and random login and password parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithFirstNameNullTest() {
        courier = new CreateCourierRequest(login, password, null);
        Response response = createCourier.createCourierResponseTest(courier);
        statusCode.return201Test(response);
        body.returnOkTrueBodyTest(response);
    }

    @Test
    @DisplayName("Create courier without login")
    @Description("Failed create courier without login and random password and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutLoginTest() {
        courierNull = new CreateCourierRequestWithoutParameters(null, password, firstName);
        Response response = createCourier.createCourierWithoutParameterResponseTest(courierNull);
        statusCode.return400Test(response);
        body.createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier without password")
    @Description("Failed create courier without password and random login and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutPasswordTest() {
        courierNull = new CreateCourierRequestWithoutParameters(login, null, firstName);
        Response response = createCourier.createCourierWithoutParameterResponseTest(courierNull);
        statusCode.return400Test(response);
        body.createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier without firstName")
    @Description("Success create courier without firstName and random login and password parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutFirstNameTest() {
        courierNull = new CreateCourierRequestWithoutParameters(login, password, null);
        Response response = createCourier.createCourierWithoutParameterResponseTest(courierNull);
        statusCode.return201Test(response);
        body.returnOkTrueBodyTest(response);
    }

    @After
    public void tearDown() {
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        if (id != null) {
            courierSteps.deleteCourier(id);
        }
    }
}
