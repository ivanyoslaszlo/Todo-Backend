package laszlo.dev.todo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class Time {


    public String getTime() {

        LocalDateTime timeobj = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd__HH:mm:ss");
        String LocalDatetime= formatter.format(timeobj);


        return LocalDatetime;

    }
}
