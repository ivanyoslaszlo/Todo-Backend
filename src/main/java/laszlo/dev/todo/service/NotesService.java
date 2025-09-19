package laszlo.dev.todo.service;

import jakarta.servlet.http.HttpSession;
import laszlo.dev.todo.repository.NotesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository){
        this.notesRepository=notesRepository;
    }

    public ResponseEntity<?> jegyzet_keszites( String note,HttpSession session){

        String username=(String) session.getAttribute("user");

        if (username == null) {
            return  ResponseEntity.status(401).body("Nem vagy belépve");
        }
        if (notesRepository.createNote(username, note)) {
           return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(500).body("Internal Error");
        }

    }

    public ResponseEntity<?> jegyzet_lekérés(HttpSession session)
    {
        String username=(String)session.getAttribute("user");
        if (username==null){
            return ResponseEntity.status(401).body("Nincs jogosultásgod megtekinteni mivel nem vagy belépve!");
        }else{
            List<String> notes =notesRepository.getNotes(username);
            return ResponseEntity.ok(notes);
        }

    }


    public ResponseEntity<?> jegyezttörlés_adatbázisbol(HttpSession session,List<String> notes){

        String user = (String) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(403).body("Nincs jogod törölni nem vagy belépve!");

        } else if (notesRepository.deleteNotes(notes, user)) {
            return ResponseEntity.ok("ok");

        } else {
            return  ResponseEntity.status(500).body("Internal error");
        }
    }
}
