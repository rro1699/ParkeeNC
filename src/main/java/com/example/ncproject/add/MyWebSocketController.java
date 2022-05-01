package com.example.ncproject.add;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyWebSocketController {
    private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void addNewUser(WebSocketSession socketSession){
        System.out.println("added new user = "+socketSession.getId());
        sessions.add(socketSession);
    }

    public void removeUser(WebSocketSession socketSession){
        System.out.println("delete user = "+socketSession.getId());
        sessions.remove(socketSession);
    }

    public static void sendMessage(String placeId){
        sessions.forEach(session -> {
            try {
                System.out.println("send message to = "+session.getId());
                session.sendMessage(new TextMessage(placeId));
            } catch (IOException e) {
                System.out.println("Couldn't send message");
            }
        });
    }

}
