package ru.yandex.practicum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.practicum.steps.dto.CreateOrderRequest;
import ru.yandex.practicum.steps.dto.OrdersCancelResponse;

import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.yandex.practicum.steps.env.EnvConf.*;


public class OrderSteps {

    public Response createOrder(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate,
                                String comment, List<String> color) {

        CreateOrderRequest order = new CreateOrderRequest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(order)
                .with()
                .post(CREATE_GET_ORDER_ENDPOINT);
    }

    public Response getOrder() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .get(CREATE_GET_ORDER_ENDPOINT);
    }

    public Response getOrderWithTrack(int track) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .queryParam("t", track)
                .when()
                .get(GET_ORDER_WITH_TRACK_ENDPOINT);
    }

    public Response getOrderWithoutTrack() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .queryParam("t", "")
                .when()
                .get(GET_ORDER_WITH_TRACK_ENDPOINT);
    }

    public Response acceptOrder(int id, int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .pathParam("id", id)
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_ENDPOINT);
    }

    public Response acceptOrderWithoutCourierId(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .pathParam("id", id)
                .when()
                .put(ACCEPT_ORDER_ENDPOINT);
    }

    public Response acceptOrderWithoutOrderId(int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .pathParam("id", "")
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_ENDPOINT);
    }

    public Response cancelOrder(int track) {
        OrdersCancelResponse cancelOrder = new OrdersCancelResponse(track);

        return given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .body(cancelOrder)
                .with()
                .put(CANCEL_ORDER_ENDPOINT);
    }
}
