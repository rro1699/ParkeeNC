package com.example.ncproject.add;

import lombok.Getter;

@Getter
public class DeleteInfo {
    private String reservationId;
    private String placeId;
    private String startTimeReser;

    public DeleteInfo(String reservationId, String placeId,String startTimeReser) {
        this.reservationId = reservationId;
        this.placeId = placeId;
        this.startTimeReser = startTimeReser;
    }
}
