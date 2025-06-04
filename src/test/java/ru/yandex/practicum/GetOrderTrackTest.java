package ru.yandex.practicum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.dto.OrdersTrackMainResponse;
import ru.yandex.practicum.steps.dto.OrdersTrackResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.practicum.steps.env.EnvConf.ORDERS_ORDER_TEST_DATA;


public class GetOrderTrackTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private static final String[] orderData = orderData();
    private Response response;
    private OrdersTrackResponse orderResponse;
    private Integer trackId;

    private static String[] orderData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(ORDERS_ORDER_TEST_DATA));
            String firstLine = lines.get(1);
            return firstLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data file", e);
        }
    }

    Response responseOrder = createOrderTest(
            orderData[0].replace("\"", ""),
            orderData[1].replace("\"", ""),
            orderData[2].replace("\"", ""),
            orderData[3].isEmpty() ? 0 : Integer.parseInt(orderData[3]),
            orderData[4].replace("\"", ""),
            orderData[5].isEmpty() ? 0 : Integer.parseInt(orderData[5]),
            orderData[6].replace("\"", ""),
            orderData[7].replace("\"", ""),
            orderData[8].isEmpty() ? List.of() : Arrays.asList(orderData[8].split("\\|"))
    );

    @Before
    public void setUp() {
        if (responseOrder.getStatusCode() == 500) {
            throw new RuntimeException("500 Internal Server Error: " + responseOrder.getBody().asString());
        }
        // Количество символов: MIN_COUNT_LENGTH + 7
        trackId = getTrackIdTest(responseOrder);;
    }

    @Test
    @DisplayName("Get order with valid track id")
    @Description("Create order. Success get order with valid track id, query parameter - t. GET \"/api/v1/orders/track\"")
    public void getOrderWithTrackIdTest() {
        response = orderSteps.getOrderWithTrack(trackId);
        orderResponse = response.as(OrdersTrackResponse.class);
        return200Test(response);
        returnOrderWithSameTrackBodyTest(response);
        getOrderReturnNotNullDeserializeTest(orderResponse);
    }

    @Step("Create order")
    public Response createOrderTest(String firstName, String lastName, String address, int metroStation, String phone,
                                    int rentTime, String deliveryDate, String comment, List<String> color) {
        return orderSteps.createOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    @Step("Get track id")
    public Integer getTrackIdTest(Response response) {
        Allure.step("Track id: " + response.then().extract().path("track"));
        return response.then().extract().path("track");
    }

    @Step("Return correct status code - 200")
    public void return200Test(Response response) {
        response.then().statusCode(200);
    }

    @Step("Get order return correct body")
    public void returnOrderWithSameTrackBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("order.track", equalTo(trackId));
    }

    @Step("Deserialize order is not null")
    public void getOrderReturnNotNullDeserializeTest(OrdersTrackResponse ordersResponse) {
        OrdersTrackMainResponse order = ordersResponse.getOrder();
        Allure.step("Order body: " + order.toString());
        Assert.assertNotNull("Orders list is null", order);
    }

    @Test
    @DisplayName("Get order without track id")
    @Description("Create order. Failed get order without track id, query parameter - t. GET \"/api/v1/orders/track\"")
    public void getOrderWithoutTrackIdTest() {
        response = orderSteps.getOrderWithoutTrack();
        return400Test(response);
        returnNotEnoughDataBodyTest(response);
    }

    @Step("Return correct status code - 400")
    public void return400Test(Response response) {
        response.then().statusCode(400);
    }

    @Step("Get order return correct body - \"message\":  \"Недостаточно данных для поиска\"")
    public void returnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Get order with wrong track id")
    @Description("Create order. Failed get order with wrong track id, query parameter - t. GET \"/api/v1/orders/track\"")
    public void getOrderWithWrongTrackIdTest() {
        response = orderSteps.getOrderWithTrack(trackId + trackId);
        return404Test(response);
        returnOrderNotFoundBodyTest(response);
    }

    @Step("Return correct status code - 404")
    public void return404Test(Response response) {
        response.then().statusCode(404);
    }

    @Step("Get order return correct body - \"message\": \"Заказ не найден\"")
    public void returnOrderNotFoundBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo("Заказ не найден"));
    }

    @After
    public void tearDown() {
        if (trackId != null) {
            orderSteps.cancelOrder(trackId);
        }
    }
}
