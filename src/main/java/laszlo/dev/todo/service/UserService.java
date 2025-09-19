package laszlo.dev.todo.service;

import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(Users user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "Hiba: minden mező kitöltése kötelező!";
        }

        if (userRepository.check_username(user.getUsername())) {
            return "Hiba: foglalt felhasználónév!";
        }

        user.setPassword(userRepository.password_hash(user.getPassword()));
        userRepository.save(user);

        return "Sikeres regisztráció: " + user.getUsername();
    }

    public String loginUser(String username, String password, HttpSession session) {

        Users user = userRepository.findByUsername(username);

        if (user == null) {
            return "Nincs ilyen felhasználó!";

        } else if (!userRepository.check_password(password, user.getPassword())) {

            return "Hibás jelszó!";
        } else if (userRepository.check_password(password, user.getPassword())) {

            session.setAttribute("user", user.getUsername());
            session.setAttribute("login_time", LocalDateTime.now().withNano(0));
            System.out.println("Bejelentkezett: " + user.getUsername() + " " + session.getAttribute("login_time"));
            userRepository.updateLastLogin(user.getUsername());

        }

        if (userRepository.is_admin(user.getUsername())) {
            return "admin";
        } else {
            return "user";
        }
    }

}
