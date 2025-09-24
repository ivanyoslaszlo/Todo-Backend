package laszlo.dev.todo.service;

import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {




        private final JavaMailSender mailSender;

        public EmailService(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }


        public void sendEmail(String kinek, String felhasznalo_nev) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("lacitodo@gmail.com");
            message.setTo(kinek);
            message.setSubject("Sikeres regisztráció - TodoApp");
            message.setText("Kedves " + felhasznalo_nev + "!\n\n"
                    + "Köszönjük, hogy regisztráltál az oldalunkon.\n"
                    + "Most már be tudsz jelentkezni.\n\n"
                    + "Üdv,\nTodoApp csapat");

            try {
                mailSender.send(message);
                System.out.println("Email kiküldve!");
            }catch (Exception e){
                System.out.println("hiba az email küldésekor: "+e);
            }



        }


}
