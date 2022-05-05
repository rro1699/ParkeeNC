package com.example.ncproject.Controllers;

import com.example.ncproject.Models.Reservation;
import com.example.ncproject.Repository.ReservationRepository;
import com.example.ncproject.add.MyTh;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

@RestController
public class ReservationController {

    private ReservationRepository reservationRepository;
    private final Semaphore semaphore = new Semaphore(1);;

    public ReservationController(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }



    //SELECT * FROM reservation WHERE placeid=9 ORDER BY endtimereser DESC LIMIT 2;

    @PostMapping(path= "/reservation/addReservation")
    public ResponseEntity AddNewPlace( @RequestHeader HttpHeaders headers, @RequestBody String reservation){
        //cookie user

        String userId = getUserId(headers.get("cookie"));
        System.out.println(userId);
        Reservation reservationReturn = getResult(reservation);
        ResponseEntity response = null;
        try {
            semaphore.acquire();
            if (reservationReturn != null) {
                //запрос к бд, не занято ли уже это время
                reservationReturn.setUserId(userId);
                reservationRepository.save(reservationReturn);
                response = new ResponseEntity(HttpStatus.CREATED);
                test(reservationReturn.getStartDateReser(),reservationReturn.getStartTimeReser(),String.valueOf(reservationReturn.getPlaceId()));
                test(reservationReturn.getEndDateReser(), reservationReturn.getEndTimeReser(), String.valueOf(reservationReturn.getPlaceId()));
            } else {
                response = new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
            }
            semaphore.release();
        } catch (InterruptedException e) {
            response = new ResponseEntity(HttpStatus.BAD_REQUEST);
            throw new RuntimeException();
        }
        finally {
            return response;
        }
    }



    private String getUserId(List<String> cookies){
        String[] names = cookies.get(0).split(";");
        String a=null;
        for(int i=0;i<names.length;i++)
        {
               if("user-id".equals(names[i].split("=")[0].trim())){
                 a = names[i].split("=")[1];
               }
        }
        return a;
    }

    private Reservation getResult(String reservation){
        Reservation reservationReturn = null;
        reservation = reservation.replaceAll("[[\\{][\\}][\"]]", "");
        String[] arrayStr = reservation.split(",");
        for(int i = 0; i < arrayStr.length; i++){
            arrayStr[i] = arrayStr[i].substring(arrayStr[i].indexOf(":") + 1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        DateTimeFormatter secondFormatter = DateTimeFormatter.ofPattern("H:m");
        try{
            reservationReturn = new Reservation(arrayStr[0],
                    Integer.parseInt(arrayStr[1]),
                    Date.valueOf(LocalDate.parse(arrayStr[2], formatter)),
                    Time.valueOf(LocalTime.parse(arrayStr[3], secondFormatter)),
                    Date.valueOf(LocalDate.parse(arrayStr[4], formatter)),
                    Time.valueOf(LocalTime.parse(arrayStr[5], secondFormatter))
            );
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return reservationReturn;
    }


    private void test(Date endData, Time endTime, String placeId){
        LocalDateTime  endDateTime = LocalDateTime.of(endData.toLocalDate(),endTime.toLocalTime());
        long delay  = Duration.between(LocalDateTime.now(),endDateTime).abs().toMillis();
        System.out.println(delay);
        MyTh th = new MyTh();
        th.setPlaceId(placeId);
        new Timer().schedule(th, delay);
    }
}
