package laszlo.dev.todo.controllers;

import jakarta.servlet.http.HttpSession;
import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.UserRepository;
import laszlo.dev.todo.service.EmailService;
import laszlo.dev.todo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class RegisterController {

    UserService userService;
    EmailService emailService;
    UserRepository userRepository;

    public RegisterController(UserService userService, EmailService emailService, UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;

    }

    @PostMapping("/register")
    public String register_user(@RequestBody Users users) {

        if (userService.registerUser(users)) {
            emailService.sendEmail(users.getEmail(), users.getUsername());
            return "siker";
        } else {
            return "foglalt";
        }


    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> reset_password(@RequestBody Map<String, String> data, HttpSession session) {

        String logged_in_user=(String) session.getAttribute("user");
        String password = data.get("password");

        if (logged_in_user == null) {
            return ResponseEntity.status(401).body("Nem vagy bejelentkezve!");
        }

        if (userService.reset_password(logged_in_user, password)) {
            return ResponseEntity.ok("Sikeres jelszó reset");
        } else {
            return ResponseEntity.status(500).body("Hiba");
        }

    }


}
