package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.GetOrderSteps;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.StatusCodeSteps;
import ru.yandex.practicum.steps.dto.OrdersResponse;


public class GetOrderListTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final GetOrderSteps getOrder = new GetOrderSteps();
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
        statusCode.return200Test(response);
        getOrder.getOrderReturnNotNullBodyTest(response);
        getOrder.getOrderReturnNotNullBodyIdTest(response);
        getOrder.getOrderTrackReturnNotNullDeserializeTest(ordersResponse);
    }
}
