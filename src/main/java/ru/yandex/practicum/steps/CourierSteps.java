package ru.yandex.practicum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.practicum.steps.dto.CreateCourierRequest;
import ru.yandex.practicum.steps.dto.CreateCourierRequestWithoutParameters;

import static io.restassured.RestAssured.given;
import static ru.yandex.practicum.steps.env.EnvConf.*;


public class CourierSteps {

    public Response createCourier(String login, String password, String firstName, boolean withoutParameters) {
        Object courier;

        if (withoutParameters) {
            courier = new CreateCourierRequestWithoutParameters(login, password, firstName);
        } else {
            courier = new CreateCourierRequest(login, password, firstName);
        }

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(courier)
                .when()
                .post(CREATE_COURIER_ENDPOINT);
    }

    public Response deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .pathParam("id", id)
                .when()
                .delete(DELETE_COURIER_ENDPOINT);
    }

    public Response deleteCourierWithoutId() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .pathParam("id", "")
                .when()
                .delete(DELETE_COURIER_ENDPOINT);
    }

    public Response loginCourier(String login, String password, boolean withoutParameters) {
        Object courier;

        if (withoutParameters) {
            courier = new CreateCourierRequestWithoutParameters(login, password);
        } else {
            courier = new CreateCourierRequest(login, password);
        }

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(courier)
                .when()
                .post(LOGIN_COURIER_ENDPOINT);
    }
}
