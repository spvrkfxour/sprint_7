package ru.yandex.practicum;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.CourierSteps;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.dto.OrdersTrackResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.practicum.steps.env.EnvConf.*;


public class AcceptOrderTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private final CourierSteps courierSteps = new CourierSteps();
    private static final String[] orderData = orderData();
    private Response response;
    private OrdersTrackResponse orderResponse;
    private String login;
    private String password;
    private String firstName;
    private Integer trackId;
    private Integer courierId;
    private Integer orderId;

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
        login = RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        password = RandomStringUtils.randomAlphanumeric((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        firstName = RandomStringUtils.randomAlphabetic((int)(Math.random() * 8) + MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT);
        createCourierTest(login, password, firstName);
        trackId = getTrackIdTest(responseOrder);
        courierId = getCourierIdTest(login, password);
        response = orderSteps.getOrderWithTrack(trackId);
        orderResponse = response.as(OrdersTrackResponse.class);
        orderId = getOrderIdTest();
    }

    @Test
    @DisplayName("Accept order with all valid parameters")
    @Description("Create order. Create courier for accept order. " +
            "Success accept order with valid order and courier parameters. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderTest() {
        Response response = orderSteps.acceptOrder(orderId, courierId);
        return200Test(response);
        returnOkTrueBodyTest(response);
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

    @Step("Create courier")
    public Response createCourierTest(String login, String password, String firstName) {
        Response response = courierSteps.createCourier(login, password, firstName, false);
        Allure.step("Response Body: " + response.getBody().asString());
        return response;
    }

    @Step("Get courier id")
    public Integer getCourierIdTest(String login, String password) {
        Allure.step("Courier id: " + courierSteps.loginCourier(login, password, false).then().extract().path("id"));
        return courierSteps.loginCourier(login, password, false).then().extract().path("id");
    }

    @Step("Return correct status code - 200")
    public void return200Test(Response response) {
        response.then().statusCode(200);
    }

    @Step("Accept order return correct body - { ok: true }")
    public void returnOkTrueBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("ok", equalTo(true));
    }

    @Step("Get order id")
    public Integer getOrderIdTest() {
        Allure.step("Order id: " + orderResponse.getOrder().getId());
        return orderId = orderResponse.getOrder().getId();
    }

    @Test
    @DisplayName("Accept order without courier id query parameter")
    @Description("Create order. Create courier for accept order. " +
            "Failed accept order without courier id query parameter. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderWithoutCourierIdTest() {
        Response response = orderSteps.acceptOrderWithoutCourierId(orderId);
        return400Test(response);
        returnNotEnoughDataBodyTest(response);
    }

    @Step("Return correct status code - 400")
    public void return400Test(Response response) {
        response.then().statusCode(400);
    }

    @Step("Accept order return correct body - \"message\": \"Недостаточно данных для поиска\"")
    public void returnNotEnoughDataBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(ACCEPT_ORDER_NOT_ENOUGH_DATA_ERROR));
    }

    @Test
    @DisplayName("Accept order with wrong courier id query parameter")
    @Description("Create order. Create courier for accept order. " +
            "Failed accept order with wrong courier id query parameter. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderWithWrongCourierIdTest() {
        Response response = orderSteps.acceptOrder(orderId, courierId + courierId);
        return404Test(response);
        returnCourierIdNotFoundBodyTest(response);
    }

    @Step("Return correct status code - 404")
    public void return404Test(Response response) {
        response.then().statusCode(404);
    }

    @Step("Accept order return correct body - \"message\": \"Курьера с таким id не существует\"")
    public void returnCourierIdNotFoundBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(ACCEPT_COURIER_ID_NOT_FOUND_ERROR));
    }

    @Test
    @DisplayName("Accept order without order id path parameter")
    @Description("Create order. Create courier for accept order. " +
            "Failed accept order without order id path parameter. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderWithoutOrderIdTest() {
        Response response = orderSteps.acceptOrderWithoutOrderId(courierId);
        return404Test(response);
        returnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Accept order with wrong order id path parameter")
    @Description("Create order. Create courier for accept order. " +
            "Failed accept order with wrong order id path parameter. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderWithWrongOrderIdTest() {
        Response response = orderSteps.acceptOrder(orderId + orderId, courierId);
        return404Test(response);
        returnOrderIdNotFoundBodyTest(response);
    }

    @Step("Accept order return correct body - \"message\": \"Заказа с таким id не существует\"")
    public void returnOrderIdNotFoundBodyTest(Response response) {
        Allure.step("Response Body: " + response.getBody().asString());
        response.then().body("message", equalTo(ACCEPT_ORDER_ID_NOT_FOUND_ERROR));
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId);
        }
        if (trackId != null) {
            orderSteps.cancelOrder(trackId);
        }
    }
}
