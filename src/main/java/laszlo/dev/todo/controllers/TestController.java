package laszlo.dev.todo.controllers;

import laszlo.dev.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController

@RequestMapping("/api")
public class TestController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok(Map.of( "message", "A backend fut !"));
    }

    @GetMapping("/database")
    public ResponseEntity<?> databasse(){

        if (!userRepository.testconnection()){
            return ResponseEntity.status(500).body(Map.of("message","mysql OFFLINE"));
        }
        else
        {
            return ResponseEntity.ok(Map.of( "message", "mysql ONLINE"));
        }
    }
}
