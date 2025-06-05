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

import static ru.yandex.practicum.steps.env.EnvConf.*;


public class CreateCourierTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final CreateCourierSteps createCourier = new CreateCourierSteps();
    private final BodySteps body = new BodySteps();
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
        Response response = createCourier.createCourierResponseTest(login, password, firstName);
        statusCode.return201Test(response);
        body.returnOkTrueBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with a login that is already taken")
    @Description("Failed create courier with random login, password and firstName parameters (3-10 char). POST \"/api/v1/courier\"." +
            " Create another courier with same login")
    public void createCourierWithAlreadyCreatedLoginTest() {
        createCourier.createCourierResponseTest(login, password, firstName);
        Response response = createCourier.createCourierResponseTest(login,
                RandomStringUtils.randomAlphanumeric((int)(Math.random() * 8) + 3),
                RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + 3));
        statusCode.return409Test(response);
        body.createCourierReturnAlreadyUseBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with login = null")
    @Description("Failed create courier with login = null and random password and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithLoginNullTest() {
        Response response = createCourier.createCourierResponseTest(null, password, firstName);
        statusCode.return400Test(response);
        body.createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with password = null")
    @Description("Failed create courier with password = null and random login and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithPasswordNullTest() {
        Response response = createCourier.createCourierResponseTest(login, null, firstName);
        statusCode.return400Test(response);
        body.createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier with firstName = null")
    @Description("Success create courier with firstName = null and random login and password parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithFirstNameNullTest() {
        Response response = createCourier.createCourierResponseTest(login, password, null);
        statusCode.return201Test(response);
        body.returnOkTrueBodyTest(response);
    }

    @Test
    @DisplayName("Create courier without login")
    @Description("Failed create courier without login and random password and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutLoginTest() {
        Response response = createCourier.createCourierWithoutParameterResponseTest(null, password, firstName);
        statusCode.return400Test(response);
        body.createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier without password")
    @Description("Failed create courier without password and random login and firstName parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutPasswordTest() {
        Response response = createCourier.createCourierWithoutParameterResponseTest(login, null, firstName);
        statusCode.return400Test(response);
        body.createCourierReturnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Create courier without firstName")
    @Description("Success create courier without firstName and random login and password parameters (3-10 char). POST \"/api/v1/courier\"")
    public void createCourierWithoutFirstNameTest() {
        Response response = createCourier.createCourierWithoutParameterResponseTest(login, password, null);
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
