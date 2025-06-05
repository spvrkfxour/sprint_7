package ru.yandex.practicum.steps.env;


public class EnvConf {
    public static final String URL = "https://qa-scooter.praktikum-services.ru/";
    public static final String CREATE_COURIER_ENDPOINT = "/api/v1/courier";
    public static final String DELETE_COURIER_ENDPOINT = "/api/v1/courier/{id}";
    public static final String LOGIN_COURIER_ENDPOINT = "/api/v1/courier/login";
    public static final String CREATE_GET_ORDER_ENDPOINT = "/api/v1/orders";
    public static final String CANCEL_ORDER_ENDPOINT = "/api/v1/orders/cancel";
    public static final String ACCEPT_ORDER_ENDPOINT = "/api/v1/orders/accept/{id}";
    public static final String GET_ORDER_WITH_TRACK_ENDPOINT = "/api/v1/orders/track";

    public static final int MIN_COUNT_RANDOM_LENGTH_FOR_SHORT_TEXT = 3;
    public static final int MIN_COUNT_RANDOM_LENGTH_FOR_LONG_TEXT = 20;
    public static final int TIMEOUT_MILLISECONDS = 5000;

    public static final String LOGIN_ALREADY_USE_ERROR = "Этот логин уже используется";
    public static final String CREATE_COURIER_NOT_ENOUGH_DATA_ERROR = "Недостаточно данных для создания учетной записи";
    public static final String LOGIN_COURIER_NOT_ENOUGH_DATA_ERROR = "Недостаточно данных для входа";
    public static final String LOGIN_DATA_NOT_FOUND_ERROR = "Учетная запись не найдена";
    public static final String DELETE_COURIER_NOT_ENOUGH_DATA_ERROR = "Недостаточно данных для удаления курьера";
    public static final String DELETE_COURIER_NOT_FOUND_DATA_ERROR = "Курьера с таким id нет";
    public static final String GET_ORDER_TRACK_ID_NOT_FOUND_ERROR = "Заказ не найден";
    public static final String GET_ORDER_TRACK_ID_NOT_ENOUGH_DATA_ERROR = "Недостаточно данных для поиска";
    public static final String ACCEPT_ORDER_ID_NOT_FOUND_ERROR = "Заказа с таким id не существует";
    public static final String ACCEPT_COURIER_ID_NOT_FOUND_ERROR = "Курьера с таким id не существует";
    public static final String ACCEPT_ORDER_NOT_ENOUGH_DATA_ERROR = "Недостаточно данных для поиска";

    public static final String ORDERS_ORDER_TEST_DATA = "src/test/resources/ordersOrderTest.csv";
}
