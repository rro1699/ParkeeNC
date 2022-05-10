package com.example.ncproject.Services.ServiceUtils;

import com.example.ncproject.WebSockets.MyWebSocketController;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
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
