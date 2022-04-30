package com.example.ncproject.add;


import org.springframework.stereotype.Controller;
import java.util.TimerTask;

@Controller
public class MyTh extends TimerTask {
    private String placeId;

    public void setPlaceId(String placeId){
        this.placeId=placeId;
    }

    @Override
    public void run() {
        System.out.println("Запускаю отправку по сокетам");
        MyWebSocketController.sendMessage(placeId);
    }

}
