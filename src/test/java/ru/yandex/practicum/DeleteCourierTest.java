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

import static ru.yandex.practicum.steps.env.EnvConf.*;


public class DeleteCourierTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final CreateCourierSteps createCourier = new CreateCourierSteps();
    private final BodySteps body = new BodySteps();
    private String login;
    private String password;
    private String firstName;
    private CreateCourierRequest courier;

    @Before
    public void setUp() {
        // Количество символов: MIN_COUNT_LENGTH + 7
        login = RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        password = RandomStringUtils.randomAlphanumeric((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        firstName = RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        courier = new CreateCourierRequest(login, password, firstName);
        createCourier.createCourierTest(courier);
    }

    @Test
    @DisplayName("Delete courier with valid id")
    @Description("Success delete courier with valid id in request. DELETE \"/api/v1/courier/{id}\"")
    public void deleteCourierTest() {
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        Response response = courierSteps.deleteCourier(id);
        statusCode.return200Test(response);
        body.returnOkTrueBodyTest(response);
    }

    @Test
    @DisplayName("Delete courier with nonexistent id")
    @Description("Failed delete courier with nonexistent id in request. DELETE \"/api/v1/courier/{id}\"")
    public void deleteCourierWithNonexistentIdTest() {
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        Response response = courierSteps.deleteCourier(id + id);
        statusCode.return404Test(response);
        body.deleteCourierReturnNotFoundDataBodyTest(response);
    }

    @Test
    @DisplayName("Delete courier without id")
    @Description("Failed delete courier without id params in request. DELETE \"/api/v1/courier/{id}\"")
    public void deleteCourierWithoutIdTest() {
        Response response = courierSteps.deleteCourierWithoutId();
        statusCode.return404Test(response);
        body.deleteCourierReturnNotEnoughDataBodyTest(response);
    }

    @After
    public void tearDown() {
        Integer id = courierSteps.loginCourier(login, password, false).then().extract().path("id");
        if (id != null) {
            courierSteps.deleteCourier(id);
        }
    }
}
