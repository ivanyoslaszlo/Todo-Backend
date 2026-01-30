package laszlo.dev.todo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.NotesRepository;
import laszlo.dev.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {
    @Autowired
    NotesRepository notesRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Mylogger logger;


    public boolean isUserExist(String username) {
        return username != null && userRepository.findByUsername(username) != null;
    }

    public ResponseEntity<?> createNote(String note, HttpSession session) {

        String username = (String) session.getAttribute("user");

        if (!isUserExist(username)) {
            session.invalidate();
            return ResponseEntity.status(401).body("You need to login!");
        }
        if (notesRepository.createNote(username, note)) {
            logger.info(username + " created this note: " + note);
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(500).body("Internal Error");
        }
    }

    public ResponseEntity<?> getNote(HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("user");

        if (!isUserExist(username)) {
            String ip = request.getRemoteAddr();
            session.invalidate();
            logger.warn("unauthorized aces attempt from ip: : " + ip);
            return ResponseEntity.status(401).body("Login required to access!");
        } else {
            List<String> notes = notesRepository.getNotes(username);
            return ResponseEntity.ok(notes);
        }
    }

    public ResponseEntity<?> deleteNote(HttpSession session, List<String> notes) {

        String user = (String) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(403).body("Login required to access!");

        } else if (notesRepository.deleteNotes(notes, user)) {
            logger.info(user + " deleted this note: " + notes);
            return ResponseEntity.ok("successfully deleted");

        } else {
            return ResponseEntity.status(500).body("Internal error");
        }
    }

    public List<Users> getNote() {
        List<Users> users = userRepository.findAllUsers();

        for (Users user : users) {
            user.setNotes(notesRepository.getNotes(user.getUsername()));
        }
        users.removeIf(user -> user.getRole().equalsIgnoreCase("admin"));
        return users;
    }

}
