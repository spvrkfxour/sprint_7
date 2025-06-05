package ru.yandex.practicum.steps.dto;

import lombok.Data;

import java.util.List;


@Data
public class OrdersMainResponse {
    private int id;
    private int courierId;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private int track;
    private List<String> color;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private String status;

    @Override
    public String toString() {
        return "OrdersMainResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", rentTime=" + rentTime +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", status='" + status + '\'' +
                ", color=" + color +
                '}';
    }
}
