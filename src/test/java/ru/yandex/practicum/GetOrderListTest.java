package ru.yandex.practicum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.dto.OrdersMainResponse;
import ru.yandex.practicum.steps.dto.OrdersResponse;

import java.util.List;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.notNullValue;


public class GetOrderListTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private Response response;
    private OrdersResponse ordersResponse;


    @Before
    public void setUp() {
        response = orderSteps.getOrder();
        ordersResponse = response.as(OrdersResponse.class);
    }

    @Test
    @DisplayName("Get orders")
    @Description("Success get orders list. GET \"/api/v1/orders\"")
    public void getOrderTest() {
        return200Test(response);
        getOrderReturnNotNullBodyTest(response);
        getOrderReturnNotNullBodyIdTest(response);
        getOrderReturnNotNullDeserializeTest(ordersResponse);
    }

    @Step("Return correct status code - 200")
    public void return200Test(Response response) {
        response.then().statusCode(200);
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

    @Step("Deserialize orders list is not null")
    public void getOrderReturnNotNullDeserializeTest(OrdersResponse ordersResponse) {
        List<OrdersMainResponse> orders = ordersResponse.getOrders();
        Allure.step("Orders list: " + orders.toString());
        Assert.assertNotNull("Orders list is null", orders);
        Assert.assertFalse("Orders list is empty", orders.isEmpty());
    }
}
