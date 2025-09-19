package laszlo.dev.todo.controllers;
import laszlo.dev.todo.service.UserService;

import jakarta.servlet.http.HttpSession;
import laszlo.dev.todo.repository.UserRepository;
import laszlo.dev.todo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NoteController {


    UserRepository userRepository;

    public NoteController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/note")
    public String jegyzet_keszites(@RequestBody Map<String, String> body, HttpSession session) {
        String note = body.get("note");

        String username = (String) session.getAttribute("user");
        if (username == null) {
            return "nincs belépve senki";
        }
        if (userRepository.createNote(username, note)) {
            return "Jegyzet létre hozva"+": "+ session.getAttribute("user");
        } else {
            return "a mentés elhasalt";
        }

    }


}
