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
    public ResponseEntity<?> createNote(@RequestBody Map<String, String> data, HttpSession session, HttpServletResponse response) {
        String note = data.get("note");

        logger.info(session.getAttribute("user")+" add new note: "+note);
        return notesService.createNote(note, session);
    }

    @GetMapping("/note")
    public ResponseEntity<?> getNote(HttpSession session, HttpServletRequest request) {

        return notesService.getNote(session,request);
    }

    @DeleteMapping("/note")
    public ResponseEntity<?> deleteNote(@RequestBody List<String> notes, HttpSession session) {

        logger.info(session.getAttribute("user")+"deleted this note: : "+notes);
        return notesService.deleteNote(session, notes);

    }
}
