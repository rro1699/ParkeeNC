package com.example.ncproject.Controllers;

import com.example.ncproject.Services.ReservationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation/currentReservation")
    public ResponseEntity getcurrentReservation(@RequestHeader HttpHeaders headers){
        return reservationService.getCurrentReservation(headers.get("cookie"));
    }

    @GetMapping("/reservation/endedReservation")
    public ResponseEntity getEndedReservations(@RequestHeader HttpHeaders headers) {
        return reservationService.getEndedReservations(headers.get("cookie"));
    }

    @PostMapping(path= "/reservation/addReservation")
    public ResponseEntity AddNewPlace( @RequestHeader HttpHeaders headers, @RequestBody String reservation){
        return reservationService.addNewReservation(headers.get("cookie"),reservation);
    }

    @DeleteMapping("/reservation/delete")
    public ResponseEntity deleteCurResevation(@RequestBody String values){
        return reservationService.deleteCurResevation(values);
    }
}
