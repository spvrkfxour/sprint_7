package ru.yandex.practicum.steps.dto;


public class OrdersCancelResponse {
    private int track;

    public OrdersCancelResponse(int track) {
        this.track = track;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }
}
