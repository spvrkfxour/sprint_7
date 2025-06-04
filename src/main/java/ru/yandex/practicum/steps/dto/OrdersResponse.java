package ru.yandex.practicum.steps.dto;

import java.util.List;


public class OrdersResponse {
    private List<OrdersMainResponse> orders;
    private OrdersPageInfoResponse pageInfo;
    private List<OrdersAvailableStations> availableStations;

    public List<OrdersMainResponse> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersMainResponse> orders) {
        this.orders = orders;
    }

    public OrdersPageInfoResponse getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(OrdersPageInfoResponse pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<OrdersAvailableStations> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<OrdersAvailableStations> availableStations) {
        this.availableStations = availableStations;
    }
}
