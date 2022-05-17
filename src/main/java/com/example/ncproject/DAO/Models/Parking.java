package com.example.ncproject.DAO.Models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Parking {
    @Id
    @Column(name = "parckingid")
    private Integer parkingId;

    private String tariffplan;

    public Parking(Integer parkingId, String tariffplan) {
        this.parkingId = parkingId;
        this.tariffplan = tariffplan;
    }

    public Parking(){
    }
}
