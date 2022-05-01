package com.example.ncproject.Controllers;

import com.example.ncproject.Models.Reservation;
import com.example.ncproject.Repository.ReservationRepository;
import com.example.ncproject.add.MyTh;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.concurrent.Semaphore;

@RestController
public class ReservationController {

    private ReservationRepository reservationRepository;
    private final Semaphore semaphore = new Semaphore(1);;

    private ReservationController(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }


   /* @GetMapping("/reservation/add")
    public void addRes(){
        List<Reservation> list = new ArrayList<>();
        Date date  = Date.valueOf(LocalDate.of(2022,4,26));
        Time time;
        Time time1;
        int UserId;
        int PlaceId;
        for(int i=0;i<23;i++){
            time = Time.valueOf(LocalTime.of(i,00,00));
            time1 = Time.valueOf(LocalTime.of(i,15,00));;
            UserId = (int)Math.round((Math.random()+1)*5);
            PlaceId = (int)Math.round((Math.random()+1)*5);
            list.add(new Reservation(UserId,PlaceId,date,time,date,time1));
        }
        Collections.shuffle(list);
        for(Reservation s : list)
            reservationRepository.save(s);
    }*/
    //SELECT * FROM reservation WHERE placeid=9 ORDER BY endtimereser DESC LIMIT 2;

   /* @GetMapping("/reservation/info")
    public String ReservationInfo(Model model){
        Iterable<Reservation> places = reservationRepository.findAll();
        places.forEach(i->{
            System.out.println(i.getReservationid()+"- "+i.getUserId()+" - "+i.getPlaceId());
        });
        System.out.println("---------");
        return "redirect:/";
    }*/

   /* @GetMapping("/reservation/addReservation")
    public String AddNewPlace(Model model){
        return "addReservation";
    }*/


    @PostMapping("/reservation/addReservation")
    public ResponseEntity AddNewPlace(@RequestBody String reservation){
        Reservation reservationReturn = getResult(reservation);
        ResponseEntity response = null;
        try {
            semaphore.acquire();
            if (reservationReturn != null) {
                //запрос к бд, не занято ли уже это время
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
            reservationReturn = new Reservation(Integer.parseInt(arrayStr[0]),
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

    /*private void test(String placeId){
        System.out.println(LocalDateTime.now()+"  запуск таски на смену цвета места брони "+placeId);
        MyTh th = new MyTh();
        th.setPlaceId(placeId);
        new Timer().schedule(th, 0);
    }*/


}
