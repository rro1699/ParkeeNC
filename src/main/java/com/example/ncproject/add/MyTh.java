package com.example.ncproject.add;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

public class MyTh extends TimerTask {
    @Override
    public void run() {
        System.out.println("Task performed on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
    }
}
