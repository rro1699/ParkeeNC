package com.example.ncproject.Services;

import com.example.ncproject.DAO.Models.Parking;
import com.example.ncproject.DAO.Models.Reservation;
import com.example.ncproject.DAO.Repository.ReservationRepository;
import com.example.ncproject.DAO.Repository.TariffRepository;
import com.example.ncproject.DAO.Utils.ModelsUtils.Hours;
import com.example.ncproject.Services.ServiceUtils.MyTimerTask;
import com.example.ncproject.DAO.Utils.ModelsUtils.DeleteInfo;
import com.example.ncproject.Services.ServiceUtils.ReservationWithCoast;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

@Service
@Slf4j
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TariffRepository tariffRepository;
    private final Semaphore semaphore = new Semaphore(1);
    private Multimap<String, Timer> timers= ArrayListMultimap.create();
    private final Optional<Parking> tariff;
    private String REGEXP_ALL_SLASHES = "[[\\{][\\}][\"]]";

    public ReservationService(ReservationRepository reservationRepository, TariffRepository tariffRepository, Optional<Parking> tariff) {
        this.reservationRepository = reservationRepository;
        this.tariffRepository = tariffRepository;
        this.tariff = tariffRepository.findById(1);
    }


    public ResponseEntity addNewReservation(List<String> cookies,String body){
        String userId = getUserId(cookies);
        Reservation reservation = getReservation(body);
        ResponseEntity response = null;
        Integer ReserId = null;
        Reservation collision;
        try {
            semaphore.acquire();
            if (reservation != null) {
                reservation.setUserId(userId);
                collision = reservationIntersection(reservation);
                if(collision==null) {
                    ReserId = reservationRepository.save(reservation).getReservationId();
                    response = ResponseEntity.status(HttpStatus.CREATED).build();
                    addNewTask(reservation.getStartDateReser(), reservation.getStartTimeReser(), String.valueOf(reservation.getPlaceId()), ReserId);
                    addNewTask(reservation.getEndDateReser(), reservation.getEndTimeReser(), String.valueOf(reservation.getPlaceId()), ReserId);
                }
                else {
                    response = ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(collision);
                }
            }
            semaphore.release();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
    private Reservation getReservation(String reservation){
        Reservation reservationReturn = null;
        reservation = reservation.replaceAll(REGEXP_ALL_SLASHES, "");
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
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return reservationReturn;
    }
    private Reservation reservationIntersection(Reservation reservation){
        Collection<Reservation> reservations = reservationRepository.findAllByPlaceIdAndStartDateReser(reservation.getPlaceId(),reservation.getStartDateReser());
        List<Reservation> result = reservations.stream().filter(o->{
           if(!(!o.getStartTimeReser().before(reservation.getEndTimeReser()) || !o.getEndTimeReser().after(reservation.getStartTimeReser()))){
                return true;}
            else{
                return false;}

        }).collect(Collectors.toList());
        if(result.isEmpty()) {
            return null;
        }else{
            return result.get(0);
        }
    }

    private void addNewTask(Date endData, Time endTime, String placeId, Integer resId){
        LocalDateTime endDateTime = LocalDateTime.of(endData.toLocalDate(),endTime.toLocalTime());
        long delay  = Duration.between(LocalDateTime.now(),endDateTime).abs().toMillis();
        log.info("Started new timer with duration: " + delay + " ms");
        MyTimerTask task = new MyTimerTask();
        task.setPlaceId(placeId);
        Timer timer = new Timer();
        timer.schedule(task, delay);
        timers.put(String.valueOf(resId),timer);
    }


    public ResponseEntity getCurrentReservation(List<String> cookies){
        if(!cookies.isEmpty()) {
            String userId = getUserId(cookies);
            return ResponseEntity.ok(getCurAndFuture(reservationRepository.findAllCurrentAndFuture(userId, Date.valueOf(LocalDate.now()))));
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }
    private List<ReservationWithCoast> getCurAndFuture(Collection<Reservation> reservations){
        Date nowDate = Date.valueOf(LocalDate.now());
        Time nowTime = Time.valueOf(LocalTime.now());
        Stream<Reservation> reservationStream =  reservations.stream().filter(o->
                ((o.getEndDateReser().after(nowDate)) || (o.getEndDateReser().equals(nowDate) && o.getEndTimeReser().after(nowTime))));
        return addedCoast(reservationStream.collect(Collectors.toList()));

    }



    public ResponseEntity getEndedReservations(List<String> cookies){
        if(!cookies.isEmpty()){
            String userId = getUserId(cookies);
            return ResponseEntity.ok(getLast15(reservationRepository.findAllEnded(userId,Date.valueOf(LocalDate.now()))));
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }
    private List<ReservationWithCoast> getLast15(Collection<Reservation> reservations){
        Date nowDate = Date.valueOf(LocalDate.now());
        Time nowTime = Time.valueOf(LocalTime.now());
        Stream<Reservation> reservationStream = reservations.stream().filter(o->
            !(o.getEndDateReser().equals(nowDate) && o.getEndTimeReser().after(nowTime)));
        return addedCoast(reservationStream.limit(15).collect(Collectors.toList()));

    }

    private List<ReservationWithCoast> addedCoast(List<Reservation> reservationList){
        List<ReservationWithCoast> resultList = new ArrayList<>();
        for(Reservation reservation :reservationList){
            resultList.add(new ReservationWithCoast(reservation,getCoast(reservation)));
        }
        return resultList;
    }


    public List<String> getCurrentReservation(){
        Date nowDate = Date.valueOf(LocalDate.now());
        Time nowTime = Time.valueOf(LocalTime.now());
        Collection<Reservation> reservations = reservationRepository.findAllCurrent(nowDate,nowTime);
        List<String> placeIdList = new ArrayList<>();
        reservations.forEach(o->{
            placeIdList.add(String.valueOf(o.getPlaceId()));
        });
        return placeIdList;
    }

    public ResponseEntity deleteCurResevation(String values)  {
        if(!"".equals(values)) {
            DeleteInfo info = getDeleteInfo(values);
            Iterator<Timer> iterator = timers.get(info.getReservationId()).iterator();

            while (iterator.hasNext()) {
                iterator.next().cancel();
            }
            timers.removeAll(info.getReservationId());
            Time nowTime = Time.valueOf(LocalTime.now());
            Date nowDate = Date.valueOf(LocalDate.now());
            Optional<Reservation> reservation = reservationRepository.findById(Integer.parseInt(info.getReservationId()));
            if (reservation.isPresent()){
                if(Time.valueOf(info.getStartTimeReser()).after(nowTime) || reservation.get().getStartDateReser().after(nowDate)){
                    reservationRepository.deleteById(Integer.parseInt(info.getReservationId()));
                } else {
                        reservation.get().setEndTimeReser(nowTime);
                        reservationRepository.save(reservation.get());
                        MyTimerTask th = new MyTimerTask();
                        th.setPlaceId(info.getPlaceId());
                        new Timer().schedule(th, 0);
                }
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            else{
                return ResponseEntity.badRequest().build();
            }
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }
    private DeleteInfo getDeleteInfo(String values){
        Gson gson = new Gson();
        return gson.fromJson(values,DeleteInfo.class);
    }

    private double getCoast(Reservation reservation){
            long intervalInHours = Duration.between(reservation.getEndTimeReser().toLocalTime(),reservation.getStartTimeReser().toLocalTime()).abs().toHours()+1;
            Gson gson = new Gson();
            double coast=0.0;
            if(tariff.isPresent()){
                Hours hours = gson.fromJson(tariff.get().getTariffplan(), Hours.class);
                Iterator<String> iterator = hours.getPrices().keySet().iterator();
                int position =-1;
                Time start;
                while (iterator.hasNext()){
                    start = Time.valueOf(LocalTime.of(Integer.valueOf(iterator.next()),0));
                    if(start.after(reservation.getStartTimeReser())){
                        break;
                    }
                    position++;
                }
                List<Integer> prices =  hours.getPrices().values().stream().collect(Collectors.toList());
                for(int i=position;i<position+intervalInHours;i++){
                    coast += prices.get(i);
                }
            }
            return coast;
    }


}
