package laszlo.dev.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    Mylogger mylogger;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void send_Email(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lacitodo@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
            mylogger.info("Email sent to: "+to+" text: "+text);
        } catch (Exception e) {
            mylogger.warn(e.getMessage());

        }
    }
    @Async
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
            mylogger.info("register email sent to "+to);
        } catch (Exception e) {
            mylogger.warn(e.getMessage());
        }


    }
    @Async
    public void sendDeletedAccountemail(String to, String username) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lacitodo@gmail.com");
        message.setTo(to);
        message.setSubject("Regisztráció törlés- TodoApp");
        message.setText("Kedves " + username + "!\n" +
                "Sikeresen törölted a fiókodat!");

        try {
            mailSender.send(message);
            mylogger.info("account delete email sent to "+to);
        } catch (Exception e) {
            mylogger.warn(e.getMessage());
        }
    }

}
