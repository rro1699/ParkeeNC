package com.example.ncproject.WebSockets;

import com.example.ncproject.Services.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class MyWebSocketController {
    private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private static final Map<String,Boolean> map = new HashMap<>();
    private ReservationService reservationService;

    public MyWebSocketController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public MyWebSocketController() {
    }

    public void Init(){
        reservationService.getCurrentReservation().forEach(o->{
            if(!map.containsKey(o)){
                map.put(o,false);
            }
            else{
                map.replace(o,!map.get(o));
            }
        });
    }

    public void addNewUser(WebSocketSession socketSession){
        log.info("added new user = "+socketSession.getId());
        sessions.add(socketSession);
        sendCurPlaces(socketSession);
    }

    public void removeUser(WebSocketSession socketSession){
        log.info("delete user = "+socketSession.getId());
        sessions.remove(socketSession);
    }

    private static void sendCurPlaces(WebSocketSession socketSession){
        for(Map.Entry<String,Boolean> entry:map.entrySet()){
            if(entry.getValue()==false) {
                try {
                    log.info("send message to = "+entry.getKey());
                    socketSession.sendMessage(new TextMessage(entry.getKey()));
                } catch (IOException e) {
                    log.error("sendCurPlaces error!");
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized static void sendMessage(String placeId){
        if(!map.containsKey(placeId)){
            map.put(placeId,false);
        }
        else{
            map.replace(placeId,!map.get(placeId));
        }
        sessions.forEach(session -> {
            try {
                log.info("send message to = "+session.getId());
                session.sendMessage(new TextMessage(placeId));
            } catch (IOException e) {
                log.error("Couldn't send message");
                e.printStackTrace();
            }
        });
    }

}
