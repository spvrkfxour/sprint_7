package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.steps.CreateOrderSteps;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.StatusCodeSteps;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static ru.yandex.practicum.steps.env.EnvConf.*;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private final StatusCodeSteps statusCode = new StatusCodeSteps();
    private final CreateOrderSteps createOrder = new CreateOrderSteps();
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    private Integer track;

    public CreateOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate,
                           String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "firstName = {0}, lastName = {1}, address = {2}, metroStation = {3}, phone = {4}, " +
            "rentTime = {5}, deliveryDate = {6}, comment = {7}, color = {8}")
    public static Object[][] data() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(ORDERS_ORDER_TEST_DATA));

        return lines.stream()
                .skip(1)
                .map(CreateOrderTest::parseCsvLine)
                .toArray(Object[][]::new);
    }

    private static Object[] parseCsvLine(String line) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        return new Object[] {
                parts[0].replace("\"", ""),  // firstName
                parts[1].replace("\"", ""),  // lastName
                parts[2].replace("\"", ""),  // address
                parts[3].isEmpty() ? 0 : Integer.parseInt(parts[3]),  // metroStation
                parts[4].replace("\"", ""),  // phone
                parts[5].isEmpty() ? 0 : Integer.parseInt(parts[5]),  // rentTime
                parts[6].replace("\"", ""),  // deliveryDate
                parts[7].replace("\"", ""),  // comment
                parts[8].isEmpty() ? List.of() : Arrays.asList(parts[8].split("\\|"))  // color
        };
    }

    @Test
    @DisplayName("Create order with different colors")
    @Description("Success create order with valid parameters from csv. POST \"/api/v1/orders\". Color parameter test only")
    public void createOrderTest() {
        Response response = createOrder.createOrderTest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        track = response.then().extract().path("track");
        statusCode.return201Test(response);
        createOrder.createOrderReturnTrackBodyTest(response);
    }

    @After
    public void tearDown() {
        if (track != null) {
            orderSteps.cancelOrder(track);
        }
    }
}
