package laszlo.dev.todo.controllers;

import jakarta.servlet.http.HttpSession;
import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.NotesRepository;
import laszlo.dev.todo.repository.UserRepository;
import laszlo.dev.todo.service.NotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AdminController {

    NotesService notesService;
    NotesRepository notesRepository;
    UserRepository userRepository;

    public AdminController(NotesService notesService,NotesRepository notesRepository,UserRepository userRepository){
        this.notesService=notesService;
        this.notesRepository=notesRepository;
        this.userRepository=userRepository;
    }

    @GetMapping("/api/user_notes")
    public ResponseEntity<?> user_details(HttpSession session){

        if (!userRepository.is_admin(session)){

            return ResponseEntity.status(403).body("Nem vagy admin nincs jogod lekérni!!");

        }else {

            List<Users> users=notesService.getAlluserwithNotes();

            return ResponseEntity.ok().body(users);

        }



    }


}
