package laszlo.dev.todo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
        private final JavaMailSender mailSender;

        public EmailService(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }
        private void send_Email(String to,String subject,String text){

            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom("lacitodo@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            try {
                mailSender.send(message);
                System.out.println("Email sent!");
            }catch (Exception e){
                System.out.println("Error: "+e);
            }
        }

        public void sendRegistrationEmail(String to, String username) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("lacitodo@gmail.com");
            message.setTo(to);
            message.setSubject("Sikeres regisztráció - TodoApp");
            message.setText("Kedves " + username + "!\n\n"
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
        public void sendDeletedAccountemail(String to, String username){

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("lacitodo@gmail.com");
            message.setTo(to);
            message.setSubject("Regisztráció törlés- TodoApp");
            message.setText("Kedves "+username+"!\n" +
                    "Sikeresen törölted a fiókodat!");

            try {
                mailSender.send(message);
                System.out.println("Email sent!");
            }catch (Exception e){
                System.out.println("Error: "+e);
            }
        }

}
