package laszlo.dev.todo.service;

import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    EmailService emailService;

    public boolean checkIfBanned(String username){

        if (userRepository.isbanned(username)){
            return true;
        }
        else{
            return false;
        }
    }

    public void banuser(String username, String action) {

        if (action.equals("ban")){
            userRepository.bannusers(username);

        }else if(action.equals("unban"))
        {
            userRepository.unbanusers(username);
        }
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(Users user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return false;
        }

        if (userRepository.check_username(user.getUsername())) {
            return false;
        }

        user.setPassword(userRepository.password_hash(user.getPassword()));
        userRepository.register_user(user);

        return true;
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

        if (userRepository.is_admin(session)) {
            return "admin";
        } else {
            return "user";
        }
    }

    public boolean reset_password(String username, String password) {

        if (userRepository.reset_password(username, password)) {
            return true;
        } else {
            return false;
        }

    }


    public boolean delete_user(String username) {

        Users user = userRepository.findByUsername(username);

        if (userRepository.delete_users(username)) {
            emailService.sendDeletedAccountemail(user.getEmail(), username);
            return true;
        } else {
            return false;
        }

    }


}

