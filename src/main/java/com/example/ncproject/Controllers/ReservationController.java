package com.example.ncproject.Controllers;

import com.example.ncproject.Models.Reservation;
import com.example.ncproject.Repository.ReservationRepository;
import com.example.ncproject.add.MyTh;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class ReservationController {

    private ReservationRepository reservationRepository;
    private final Semaphore semaphore = new Semaphore(1);
    private Multimap<Integer,Timer> timers= ArrayListMultimap.create();


    public ReservationController(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }



    @GetMapping("/reservation/currentReservation")
    public ResponseEntity getcurrentReservation(@RequestHeader HttpHeaders headers){
        String userId = getUserId(headers.get("cookie"));
        return ResponseEntity.ok(getCurAndFuture(reservationRepository.findAllCurrentAndFuture(userId,Date.valueOf(LocalDate.now()))));
    }

    @GetMapping("/reservation/endedReservation")
    public ResponseEntity getEndedReservations(@RequestHeader HttpHeaders headers){
        String userId = getUserId(headers.get("cookie"));
        return ResponseEntity.ok(getLast15(reservationRepository.findAllEnded(userId,Date.valueOf(LocalDate.now()))));

    }




    @PostMapping(path= "/reservation/addReservation")
    public ResponseEntity AddNewPlace( @RequestHeader HttpHeaders headers, @RequestBody String reservation){
        String userId = getUserId(headers.get("cookie"));
        System.out.println(userId);
        Reservation reservationReturn = getResult(reservation);
        ResponseEntity response = null;
        Integer ReserId = null;
        try {
            semaphore.acquire();
            if (reservationReturn != null) {
                //запрос к бд, не занято ли уже это время
                reservationReturn.setUserId(userId);
                ReserId = reservationRepository.save(reservationReturn).getReservationId();
                System.out.println(ReserId);
                response = ResponseEntity.status(HttpStatus.CREATED).build();
                test(reservationReturn.getStartDateReser(),reservationReturn.getStartTimeReser(),String.valueOf(reservationReturn.getPlaceId()),ReserId);
                test(reservationReturn.getEndDateReser(), reservationReturn.getEndTimeReser(), String.valueOf(reservationReturn.getPlaceId()),ReserId);
            } else {
                response = ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
            }
            semaphore.release();
        } catch (InterruptedException e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            throw new RuntimeException();
        }
        finally {
            return response;
        }
    }



    @GetMapping("res/delete")
    public void deleteAllReservations(){
        System.out.println(timers.size());
        Set<Integer> keys = timers.keySet();
        Iterator<Integer> iterator = keys.iterator();
        Integer val;
        Iterator<Timer> iterator1;
        while(iterator.hasNext()){
            val = iterator.next();
            iterator1 = timers.get(val).iterator();
            while (iterator1.hasNext()){
                iterator1.next().cancel();
            }
        }
    }

    private List<Reservation> getCurAndFuture(Collection<Reservation> reservations){
        return reservations.stream().filter(o->{
            if(o.getEndDateReser().equals(Date.valueOf(LocalDate.now())) && o.getEndTimeReser().after(Time.valueOf(LocalTime.now())))
                return true;
            else
                return false;
        }).collect(Collectors.toList());
    }

    private List<Reservation> getLast15(Collection<Reservation> reservations){
        long size = reservations.size();
        Stream<Reservation> reservationStream = reservations.stream().filter(o->{
            if(o.getEndDateReser().equals(Date.valueOf(LocalDate.now())) && o.getEndTimeReser().after( Time.valueOf(LocalTime.now())))
                return false;
            else
                return true;
        });
        if(size>15){
            return reservationStream.limit(15).collect(Collectors.toList());
        }else{
            return reservationStream.collect(Collectors.toList());
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

    private void test(Date endData, Time endTime, String placeId, Integer resId){
        LocalDateTime  endDateTime = LocalDateTime.of(endData.toLocalDate(),endTime.toLocalTime());
        long delay  = Duration.between(LocalDateTime.now(),endDateTime).abs().toMillis();
        System.out.println(delay);
        MyTh th = new MyTh();
        th.setPlaceId(placeId);
        Timer timer = new Timer();
        timer.schedule(th, delay);
        timers.put(resId,timer);
        System.out.println(timers.size());
        //для отмены нужен idResevation
    }
}
