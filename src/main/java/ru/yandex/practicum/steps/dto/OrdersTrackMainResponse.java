package ru.yandex.practicum.steps.dto;

import lombok.Data;

import java.util.List;


@Data
public class OrdersTrackMainResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private int track;
    private int status;
    private List<String> color;
    private String comment;
    private boolean cancelled;
    private boolean finished;
    private boolean inDelivery;
    private String courierFirstName;
    private String createdAt;
    private String updatedAt;

    @Override
    public String toString() {
        return "OrdersTrackMainResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", metroStation='" + metroStation + '\'' +
                ", phone='" + phone + '\'' +
                ", rentTime=" + rentTime +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", track=" + track +
                ", status=" + status +
                ", color=" + color +
                ", comment='" + comment + '\'' +
                ", cancelled=" + cancelled +
                ", finished=" + finished +
                ", inDelivery=" + inDelivery +
                ", courierFirstName='" + courierFirstName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
