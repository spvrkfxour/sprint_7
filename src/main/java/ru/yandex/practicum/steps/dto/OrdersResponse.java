package ru.yandex.practicum.steps.dto;

import lombok.Data;

import java.util.List;


@Data
public class OrdersResponse {
    private List<OrdersMainResponse> orders;
    private OrdersPageInfoResponse pageInfo;
    private List<OrdersAvailableStations> availableStations;
}
