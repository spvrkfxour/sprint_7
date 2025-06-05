package ru.yandex.practicum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;


public class CreateOrderSteps {
    private final OrderSteps orderSteps = new OrderSteps();

    @Step("Create order")
    public Response createOrderTest(String firstName, String lastName, String address, int metroStation, String phone,
                                    int rentTime, String deliveryDate, String comment, List<String> color) {
        return orderSteps.createOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    @Step("Create order body return track id")
    public void createOrderReturnTrackBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("track", notNullValue());
    }
}
