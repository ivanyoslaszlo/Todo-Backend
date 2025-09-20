package laszlo.dev.todo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController

public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMethod(@RequestBody Map<String, String> payload, HttpSession session, HttpServletRequest request) {
        String username = payload.get("username");
        String password = payload.get("password");

        String result = userService.loginUser(username, password, session);



        if ("Nincs ilyen felhasználó!".equals(result)) {

            return ResponseEntity.status(404).body(Map.of("message", "Nincs ilyen felhasználó!"));

        } else if ("Hibás jelszó!".equals(result)) {

            return ResponseEntity.status(401).body(Map.of("message", "Hibás jelszó!"));

        } else if (result.equals("user")) {

            return ResponseEntity.ok(Map.of("role", "user"));

        } else if (result.equals("admin")) {

            return ResponseEntity.ok(Map.of("role", "admin"));

        } else{

            return ResponseEntity.badRequest().body(Map.of("message", "Ismeretlen hiba"));
        }



    }


    @PostMapping("/kilepes")
    public ResponseEntity<?> logoutMethod(HttpSession session) {
        System.out.println("Kilépet: "+session.getAttribute("user")+ LocalDateTime.now().withNano(0));
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Sikeres kilépés"));
    }


}
