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


    public boolean letezo_user(String username) {
        return username != null && userRepository.findByUsername(username) != null;
    }

    public ResponseEntity<?> jegyzet_keszites(String note, HttpSession session) {

        String username = (String) session.getAttribute("user");

        if (!letezo_user(username)) {
            session.invalidate();
            return ResponseEntity.status(401).body("Nem vagy belépve");
        }
        if (notesRepository.createNote(username, note)) {
            logger.info(username +" létrehozta ezt a jegyzetet: "+note);
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(500).body("Internal Error");
        }

    }

    public ResponseEntity<?> jegyzet_lekérés(HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("user");

        if (!letezo_user(username)) {
           String ip= request.getRemoteAddr();
            session.invalidate();
            logger.warn("jogosulatlan hozzáférési kisérlet: "+ ip);
            return ResponseEntity.status(401).body("Nincs jogosultásgod megtekinteni mivel nem vagy belépve!");
        } else {
            List<String> notes = notesRepository.getNotes(username);
            return ResponseEntity.ok(notes);
        }

    }

    public ResponseEntity<?> jegyezttörlés_adatbázisbol(HttpSession session, List<String> notes) {

        String user = (String) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(403).body("Nincs jogod törölni nem vagy belépve!");

        } else if (notesRepository.deleteNotes(notes, user)) {

            logger.info(user+" törölte ezt a jegyzetet: "+ notes);
            return ResponseEntity.ok("Törlés sikeres");

        } else {
            return ResponseEntity.status(500).body("Internal error");
        }
    }

    public List<Users> felhasznalok_jegyzetek_listazasa() {

        List<Users> users = userRepository.findAllUsers();

        for (Users user : users) {

            user.setNotes(notesRepository.getNotes(user.getUsername()));

        }
        users.removeIf(user -> user.getRole().equalsIgnoreCase("admin"));
        return users;
    }

}
