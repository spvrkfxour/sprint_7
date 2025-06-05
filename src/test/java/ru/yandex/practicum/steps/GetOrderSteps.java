package ru.yandex.practicum.steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;
import ru.yandex.practicum.steps.dto.OrdersMainResponse;
import ru.yandex.practicum.steps.dto.OrdersResponse;
import ru.yandex.practicum.steps.dto.OrdersTrackMainResponse;
import ru.yandex.practicum.steps.dto.OrdersTrackResponse;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;


public class GetOrderSteps {

    @Step("Get track id")
    public Integer getTrackIdTest(Response response) {
        Allure.step("Track id: " + response.then().extract().path("track"));
        return response.then().extract().path("track");
    }

    @Step("Get orders body return orders")
    public void getOrderReturnNotNullBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("orders", notNullValue());
    }

    @Step("Get orders body return orders id")
    public void getOrderReturnNotNullBodyIdTest(Response response) {
        response.then().body("orders.id", everyItem(notNullValue()));
    }

    @Step("Get order return correct body")
    public void returnOrderWithSameTrackBodyTest(Response response, Integer trackId) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("order.track", equalTo(trackId));
    }

    @Step("Get order id")
    public Integer getOrderIdTest(OrdersTrackResponse orderResponse) {
        Allure.step("Order id: " + orderResponse.getOrder().getId());
        Integer orderId;
        return orderId = orderResponse.getOrder().getId();
    }

    @Step("Deserialize order is not null")
    public void getOrderTrackReturnNotNullDeserializeTest(OrdersTrackResponse ordersResponse) {
        OrdersTrackMainResponse order = ordersResponse.getOrder();
        Allure.step("Order body: " + order.toString());
        Assert.assertNotNull("Orders list is null", order);
    }

    @Step("Deserialize orders list is not null")
    public void getOrderTrackReturnNotNullDeserializeTest(OrdersResponse ordersResponse) {
        List<OrdersMainResponse> orders = ordersResponse.getOrders();
        Allure.step("Orders list: " + orders.toString());
        Assert.assertNotNull("Orders list is null", orders);
        Assert.assertFalse("Orders list is empty", orders.isEmpty());
    }
}
