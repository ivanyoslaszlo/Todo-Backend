package laszlo.dev.todo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import laszlo.dev.todo.service.NotesService;
import laszlo.dev.todo.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    NotesService notesService;


    @PostMapping("/note")
    public ResponseEntity<?> jegyzet_keszites(@RequestBody Map<String, String> data, HttpSession session, HttpServletResponse response) {



        String note = data.get("note");
        logger.info(session.getAttribute("user")+" új jegyzetet adott hozzá: "+note);
        return notesService.createNotes(note, session);

    }


    @GetMapping("/note")
    public ResponseEntity<?> jegyzet_lekeres(HttpSession session, HttpServletRequest request) {

        return notesService.getNotes(session,request);
    }


    @DeleteMapping("/note")
    public ResponseEntity<?> jegyzet_torles_adatbazisbol(@RequestBody List<String> notes, HttpSession session) {

        logger.info(session.getAttribute("user")+" törölte az alábbi jegyzetet: "+notes);
        return notesService.deleteNotes(session, notes);

    }
}
