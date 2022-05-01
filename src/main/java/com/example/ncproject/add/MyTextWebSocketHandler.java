package com.example.ncproject.add;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class MyTextWebSocketHandler extends TextWebSocketHandler {
    private final MyWebSocketController myWebSocketController = new MyWebSocketController();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        myWebSocketController.addNewUser(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        myWebSocketController.removeUser(session);
    }

}
