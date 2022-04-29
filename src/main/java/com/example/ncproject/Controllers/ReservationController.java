package com.example.ncproject.Controllers;

import com.example.ncproject.Models.Reservation;
import com.example.ncproject.Repository.ReservationRepository;
import com.example.ncproject.add.MyTh;
import org.springframework.http.HttpStatus;
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

@RestController
public class ReservationController {

    private ReservationRepository reservationRepository;

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
    //userId placeId timeStart timeEnd
    //@ResponseStatus(code = HttpStatus.CREATED)
    public HttpStatus AddNewPlace(@RequestBody String reservation){
        Reservation reservationReturn = getResult(reservation);
        HttpStatus status =null;
        //блок синхронизации
        if(reservationReturn!=null){
            reservationRepository.save(reservationReturn);
            status = HttpStatus.CREATED;
            test(reservationReturn.getEndDateReser(),reservationReturn.getEndTimeReser());
        }
        else{
            status = HttpStatus.NOT_IMPLEMENTED;
        }
        return status;
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


    public void test(Date endData, Time endTime){
            System.out.println("cur: = "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            LocalDateTime  endDateTime = LocalDateTime.of(endData.toLocalDate(),endTime.toLocalTime());
            long delay  = Duration.between(LocalDateTime.now(),endDateTime).toMillis();
            new Timer().schedule(new MyTh(), delay);
    }

}
