package laszlo.dev.todo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import laszlo.dev.todo.service.Mylogger;
import laszlo.dev.todo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api")
@RestController
public class LoginController {
    @Autowired
    Mylogger logger;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginMethod(@RequestBody Map<String, String> payload, HttpSession session, HttpServletRequest request) {
        String username = payload.get("username");
        String password = payload.get("password");

        String result = userService.loginUser(username, password, session, request);

        if ("USER_NOTFOUND".equals(result)) {
            logger.warn("Login attempt with non-existent user: " + username);
            return ResponseEntity.status(404).body(Map.of("message", "USER_NOTFOUND"));

        } else if (userService.checkIfBanned(username)) {
            logger.warn("Login attempt by blocked user:" + username);
            return ResponseEntity.status(403).body(Map.of("message", "BLOCKED_ACCOUNT"));

        } else if ("WRONG_PASSWORD".equals(result)) {
            return ResponseEntity.status(401).body(Map.of("message", "WRONG_PASSWORD"));

        } else if (result.equals("user")) {
            return ResponseEntity.ok(Map.of("role", "user"));

        } else if (result.equals("admin")) {
            return ResponseEntity.ok(Map.of("role", "admin"));

        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "UNKNOWN_ERROR"));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logoutMethod(HttpSession session) {

        logger.info(session.getAttribute("user") + " logged out");

        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "login success"));
    }


}
