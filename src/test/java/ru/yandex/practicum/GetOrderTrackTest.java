package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.*;
import ru.yandex.practicum.steps.dto.OrdersTrackResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static ru.yandex.practicum.steps.env.EnvConf.*;


public class GetOrderTrackTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final CreateOrderSteps createOrder = new CreateOrderSteps();
    private final GetOrderSteps getOrder = new GetOrderSteps();
    private final BodySteps body = new BodySteps();
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
        trackId = getOrder.getTrackIdTest(responseOrder);;
    }

    @Test
    @DisplayName("Get order with valid track id")
    @Description("Create order. Success get order with valid track id, query parameter - t. GET \"/api/v1/orders/track\"")
    public void getOrderWithTrackIdTest() {
        response = orderSteps.getOrderWithTrack(trackId);
        orderResponse = response.as(OrdersTrackResponse.class);
        statusCode.return200Test(response);
        getOrder.returnOrderWithSameTrackBodyTest(response, trackId);
        getOrder.getOrderTrackReturnNotNullDeserializeTest(orderResponse);
    }

    @Test
    @DisplayName("Get order without track id")
    @Description("Create order. Failed get order without track id, query parameter - t. GET \"/api/v1/orders/track\"")
    public void getOrderWithoutTrackIdTest() {
        response = orderSteps.getOrderWithoutTrack();
        statusCode.return400Test(response);
        body.returnNotEnoughDataBodyTest(response);
    }

    @Test
    @DisplayName("Get order with wrong track id")
    @Description("Create order. Failed get order with wrong track id, query parameter - t. GET \"/api/v1/orders/track\"")
    public void getOrderWithWrongTrackIdTest() {
        response = orderSteps.getOrderWithTrack(trackId + trackId);
        statusCode.return404Test(response);
        body.returnOrderNotFoundBodyTest(response);
    }

    @After
    public void tearDown() {
        if (trackId != null) {
            orderSteps.cancelOrder(trackId);
        }
    }
}
