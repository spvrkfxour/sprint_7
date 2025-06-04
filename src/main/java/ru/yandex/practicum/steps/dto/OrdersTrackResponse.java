package ru.yandex.practicum.steps.dto;


public class OrdersTrackResponse {
    private OrdersTrackMainResponse order;

    public OrdersTrackMainResponse getOrder() {
        return order;
    }

    public void setOrder(OrdersTrackMainResponse order) {
        this.order = order;
    }
}
