package laszlo.dev.todo.controllers;

import laszlo.dev.todo.repository.UserRepository;
import laszlo.dev.todo.service.Mylogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Mylogger mylogger;

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {

        Map<String, String> ping = new LinkedHashMap<>();
        ping.put("Backend", "Running");

        if (userRepository.testconnection()) {
            ping.put("Database", "Online");
        } else {
            ping.put("Database", "Offline");
        }
        return ResponseEntity.ok().body(ping);
    }


}
