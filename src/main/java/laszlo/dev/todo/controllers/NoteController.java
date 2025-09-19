package laszlo.dev.todo.controllers;

import laszlo.dev.todo.repository.NotesRepository;
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
    NotesRepository notesRepository;

    public NoteController(UserRepository userRepository,NotesRepository notesRepository) {
        this.userRepository = userRepository;
        this.notesRepository=notesRepository;
    }

    @PostMapping("/note")
    public String jegyzet_keszites(@RequestBody Map<String, String> body, HttpSession session) {
        String note = body.get("note");

        String username = (String) session.getAttribute("user");
        if (username == null) {
            return "nincs belépve senki";
        }
        if (notesRepository.createNote(username, note)) {
            return "Jegyzet létre hozva" + ": " + session.getAttribute("user");
        } else {
            return "a mentés sikertelen";
        }

    }


    @GetMapping("/note")
    public List<String> jegyzet_lekeres(HttpSession session) {
        String user = (String) session.getAttribute("user");

        return notesRepository.getNotes(user);
    }


    @DeleteMapping("/note")
    public String jegyzet_torles_adatbazisbol(@RequestBody List<String> notes, HttpSession session) {

        String user = (String) session.getAttribute("user");

        if (user == null) {
            return "nincs belépve senki törlés meg tagadva";

        } else if (notesRepository.deleteNotes(notes, user)) {
            return "torles sikeres";

        } else {
            return "torles sikeretelen";
        }

    }
}
