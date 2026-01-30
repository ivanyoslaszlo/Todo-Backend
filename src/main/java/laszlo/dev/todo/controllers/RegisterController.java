package laszlo.dev.todo.controllers;

import jakarta.servlet.http.HttpSession;
import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.UserRepository;
import laszlo.dev.todo.service.EmailService;
import laszlo.dev.todo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/api")
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    UserService userService;
    EmailService emailService;
    UserRepository userRepository;

    public RegisterController(UserService userService, EmailService emailService, UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public String register_user(@RequestBody Users user) {
        if (userService.registerUser(user)) {

            logger.info(user.getUsername()+" successfully registered");
            try {
                emailService.sendRegistrationEmail(user.getEmail(),user.getUsername());
                logger.info("Registration email sent to:"+user.getUsername());
            }catch (Exception e){
                logger.warn(e.getMessage());
            }
            return "success";
        } else {
            logger.warn("Attempted to register with an already taken username:  " + user.getUsername());
            return "This username already taken";
        }
    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> change_password(@RequestBody Map<String, String> data, HttpSession session) {

        String logged_in_user = (String) session.getAttribute("user");
        String password = data.get("password");

        if (logged_in_user == null) {
            return ResponseEntity.status(401).body("You need to login!");
        }
        if (userService.reset_password(logged_in_user, password)) {
            return ResponseEntity.ok("successfully password reset");
        } else {
            return ResponseEntity.status(500).body("error");
        }
    }

    @PostMapping("/delete_user")
    public ResponseEntity<?> delete_user(HttpSession session) {
        String username = (String) session.getAttribute("user");

        if (username==null){
            return ResponseEntity.status(403).body("permission denied");
        }
        if (userService.delete_user(username)) {
            session.invalidate();
            return ResponseEntity.ok().body("User deleted");
        }
        else{
            return  ResponseEntity.status(500).body("Internal Error");
        }
    }
}
