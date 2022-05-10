package com.example.ncproject.DAO.Models;


import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class Places {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placeid")
    private Integer id;

    @Column(name = "xcoor")
    private Double xCoor;

    @Column(name = "ycoor")
    private Double yCoor;

    @Column(name = "orient")
    private String orient;

    @Column(name = "parkingid")
    private Integer parkingId;

    public Places(Double xCoor, Double yCoor, String orient, Integer parkingId) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.orient = orient;
        this.parkingId = parkingId;
    }

    public Places() {
    }
}
