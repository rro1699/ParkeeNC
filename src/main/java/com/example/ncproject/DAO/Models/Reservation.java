package com.example.ncproject.DAO.Models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservationid")
    private Integer reservationId;

    @Column(name="userid")
    private String UserId;

    @Column(name="placeid")
    private Integer PlaceId;

    @Column(name = "startdatereser")
    private Date startDateReser;

    @Column(name = "starttimereser")
    private Time startTimeReser;

    @Column(name = "enddatereser")
    private Date endDateReser;

    @Column(name = "endtimereser")
    private Time endTimeReser;

    public Reservation(String userId, Integer placeId, Date startDateReser,
                       Time startTimeReser, Date endDateReser, Time endTimeReser) {
        UserId = userId;
        PlaceId = placeId;
        this.startDateReser = startDateReser;
        this.startTimeReser = startTimeReser;
        this.endDateReser = endDateReser;
        this.endTimeReser = endTimeReser;
    }

    public Reservation(Reservation reservation){
        reservationId = reservation.getReservationId();
        UserId = reservation.getUserId();
        PlaceId = reservation.getPlaceId();
        this.startDateReser = reservation.getStartDateReser();
        this.startTimeReser = reservation.getStartTimeReser();
        this.endDateReser = reservation.getEndDateReser();
        this.endTimeReser = reservation.getEndTimeReser();
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "UserId=" + UserId +
                ", PlaceId=" + PlaceId +
                ", startDateReser=" + startDateReser +
                ", startTimeReser=" + startTimeReser +
                ", endDateReser=" + endDateReser +
                ", endTimeReser=" + endTimeReser +
                '}';
    }

    public Reservation() {
    }


}
