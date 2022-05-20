package com.example.ncproject.DAO.Utils.ModelsUtils;

import lombok.Getter;

@Getter
public class DeleteInfo {
    private String reservationId;
    private String placeId;
    private String startTimeReser;
    private String startDateReser;

    public DeleteInfo(String reservationId, String placeId,String startTimeReser, String startDateReser) {
        this.reservationId = reservationId;
        this.placeId = placeId;
        this.startTimeReser = startTimeReser;
        this.startDateReser = startDateReser;
    }
}
