package laszlo.dev.todo;

import laszlo.dev.todo.service.EmailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class TodoApplication {

	public static void main(String[] args) {

		 SpringApplication.run(TodoApplication.class, args);



	}




}
