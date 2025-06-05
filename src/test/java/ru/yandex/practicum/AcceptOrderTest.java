package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.*;
import ru.yandex.practicum.steps.dto.CreateCourierRequest;
import ru.yandex.practicum.steps.dto.CreateCourierRequestWithoutParameters;
import ru.yandex.practicum.steps.dto.OrdersTrackResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static ru.yandex.practicum.steps.env.EnvConf.*;


public class AcceptOrderTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private final CourierSteps courierSteps = new CourierSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final CreateOrderSteps createOrder = new CreateOrderSteps();
    private final GetOrderSteps getOrder = new GetOrderSteps();
    private final CreateCourierSteps createCourier = new CreateCourierSteps();
    private final GetCourierSteps getCourier = new GetCourierSteps();
    private final BodySteps body = new BodySteps();
    private static final String[] orderData = orderData();
    private Response response;
    private OrdersTrackResponse orderResponse;
    private String login;
    private String password;
    private String firstName;
    private Integer trackId;
    private Integer courierId;
    private Integer orderId;
    private CreateCourierRequest courier;
    private CreateCourierRequestWithoutParameters courierNull;

    private static String[] orderData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(ORDERS_ORDER_TEST_DATA));
            String firstLine = lines.get(1);
            return firstLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data file", e);
        }
    }

    Response responseOrder = createOrder.createOrderTest(
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
        courier = new CreateCourierRequest(login, password, firstName);
        createCourier.createCourierTest(courier);
        trackId = getOrder.getTrackIdTest(responseOrder);
        courierId = getCourier.getCourierIdTest(login, password);
        response = orderSteps.getOrderWithTrack(trackId);
        orderResponse = response.as(OrdersTrackResponse.class);
        orderId = getOrder.getOrderIdTest(orderResponse);
    }

    @Test
    @DisplayName("Accept order with all valid parameters")
    @Description("Create order. Create courier for accept order. " +
            "Success accept order with valid order and courier parameters. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderTest() {
        Response response = orderSteps.acceptOrder(orderId, courierId);
        statusCode.return200Test(response);
        body.returnOkTrueBodyTest(response);
    }

    @Test
    @DisplayName("Accept order without courier id query parameter")
    @Description("Create order. Create courier for accept order. " +
            "Failed accept order without courier id query parameter. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderWithoutCourierIdTest() {
        Response response = orderSteps.acceptOrderWithoutCourierId(orderId);
        statusCode.return400Test(response);
        body.returnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Accept order with wrong courier id query parameter")
    @Description("Create order. Create courier for accept order. " +
            "Failed accept order with wrong courier id query parameter. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderWithWrongCourierIdTest() {
        Response response = orderSteps.acceptOrder(orderId, courierId + courierId);
        statusCode.return404Test(response);
        body.returnCourierIdNotFoundBodyTest(response);
    }

    @Test
    @DisplayName("Accept order without order id path parameter")
    @Description("Create order. Create courier for accept order. " +
            "Failed accept order without order id path parameter. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderWithoutOrderIdTest() {
        Response response = orderSteps.acceptOrderWithoutOrderId(courierId);
        statusCode.return404Test(response);
        body.returnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Accept order with wrong order id path parameter")
    @Description("Create order. Create courier for accept order. " +
            "Failed accept order with wrong order id path parameter. PUT \"/api/v1/orders/accept/{id}\"")
    public void acceptOrderWithWrongOrderIdTest() {
        Response response = orderSteps.acceptOrder(orderId + orderId, courierId);
        statusCode.return404Test(response);
        body.returnOrderIdNotFoundBodyTest(response);
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
