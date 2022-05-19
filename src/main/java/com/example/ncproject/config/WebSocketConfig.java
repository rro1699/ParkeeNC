package com.example.ncproject.config;


import com.example.ncproject.Services.ReservationService;
import com.example.ncproject.WebSockets.MyTextWebSocketHandler;
import com.example.ncproject.WebSockets.MyWebSocketController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@EnableWebMvc
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${VALUES}")
    private String Values;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyTextWebSocketHandler(), Values)
                .setAllowedOrigins("*");
    }

    @Bean
    public MyWebSocketController getWebSocketController(ReservationService reservationService){
        MyWebSocketController myWebSocketController = new MyWebSocketController(reservationService);
        myWebSocketController.Init();
        return myWebSocketController;
    }
}
