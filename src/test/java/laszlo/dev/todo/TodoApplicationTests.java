package laszlo.dev.todo;

import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.NotesRepository;
import laszlo.dev.todo.repository.UserRepository;
import laszlo.dev.todo.service.EmailService;
import laszlo.dev.todo.service.Mylogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotesRepository notesRepository;
    @Autowired
    private Mylogger mylogger;
    @Autowired
    private EmailService emailService;
    /*@Test
    void user_delete(){
        boolean result= userRepository.delete_users("Misi");
        assertTrue(result,"Felhasználó törölve");
    }

     */

    /*
        @Test
        void id() {
            int id = userRepository.get_userID("laci");
            System.out.println("Id:" + id);
            assertTrue(id>0);
        }


     */
    /*
    @Test
    void create_notes(){

        boolean result= notesRepository.createNote("detti","proba1");
        assertTrue(result,"Hozzá adás sikertelen!");
    }

     */
    /*
@Test
void testGetNotes() {

    String username = "laci";
    notesRepository.createNote(username, "hetedik jegyzet");
    notesRepository.createNote(username, "nyolcadik jegyzet");


    List<String> notes = notesRepository.getNotes(username);


    assertNotNull(notes);

    assertTrue(notes.contains("hetedik jegyzet"));
    assertTrue(notes.contains("nyolcadik jegyzet"));



    @Test
    void ban() {
        String username ="laci";

        userRepository.bannusers(username);

    }
    @Test
    void unban() {
        String username ="laci";

        userRepository.unbanusers(username);

    }

     */

    @Test
    void log() {


        String text = "ez itt egy warn log";

        try {
            boolean result = mylogger.warn(text);
            assertTrue(result, "Hiba");
        } catch (Exception e) {
            e.printStackTrace();

            fail("Kivétel történt: " + e.getMessage());
        }
    }


  @Test
    void test(){
        emailService.sendRegistrationEmail("ivanyoslaszlo25@gmail.com","Laci");
  }

}



