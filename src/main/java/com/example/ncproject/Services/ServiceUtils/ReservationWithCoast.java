package com.example.ncproject.Services.ServiceUtils;

import com.example.ncproject.DAO.Models.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ReservationWithCoast extends Reservation implements Serializable {
    private double coast;

    public ReservationWithCoast(Reservation reservation, double coast) {
        super(reservation);
        this.coast = coast;
    }
}
