package com.example.ncproject.add;


import java.util.TimerTask;

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
