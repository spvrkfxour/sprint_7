package ru.yandex.practicum.steps.dto;

import lombok.Data;


@Data
public class OrdersPageInfoResponse {
    private int page;
    private int total;
    private int limit;
}
