package laszlo.dev.todo.service;

import jakarta.servlet.http.HttpServletRequest;
import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    Mylogger logger;
    @Autowired
    EmailService emailService;

    public boolean checkIfBanned(String username) {

        return (userRepository.isbanned(username));
    }

    public void banuser(String username, String action) {

        if (action.equals("ban")) {
            userRepository.bannusers(username);

        } else if (action.equals("unban")) {
            userRepository.unbanusers(username);
        }
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
        logger.info(user.getUsername()+" regisztrált");

        return true;
    }

    public String loginUser(String username, String password, HttpSession session, HttpServletRequest request) {

        Users user = userRepository.findByUsername(username);
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (user == null) {

            return "Nincs ilyen felhasználó!";

        } else if (!userRepository.check_password(password, user.getPassword())) {

            logger.warn(username + " hibás jelszot adott meg!");
            return "Hibás jelszó!";

        } else if (userRepository.check_password(password, user.getPassword())) {

            session.setAttribute("user", user.getUsername());
            logger.info(session.getAttribute("user") + " belépett"+" IP: "+ip);
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

            logger.info(username + " megváltoztatta a jelszavát");
            return true;
        }
        return false;
    }


    public boolean delete_user(String username) {

        Users user = userRepository.findByUsername(username);

        if (userRepository.delete_users(username)) {
            emailService.sendDeletedAccountemail(user.getEmail(), username);
            logger.info(username + " törölte a fiókját");
            return true;
        } else {
            return false;
        }

    }


}

