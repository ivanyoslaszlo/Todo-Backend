package laszlo.dev.todo.repository;

import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotesRepository {

    @Autowired
    UserRepository userRepository;


    private Connection getConnection() throws SQLException {

        final String url = "jdbc:mysql://localhost:3306/user_datas";
        String username = "laci";
        String password = System.getenv("MYSQL_PASSWORD");
        return DriverManager.getConnection(url, username, password);
    }



    public boolean createNote(String username, String content) {
        String sql = "INSERT INTO notes (content, user_id) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, userRepository.get_userID(username));

            int succes = ps.executeUpdate();
            if (succes > 0) {
                return true;
            }


        } catch (SQLException e) {
            System.out.println("Hiba a jegyzet mentése közben: " + e.getMessage());


        }
        return false;
    }

    public List<String> getNotes(String username) {

        List<String> notes = new ArrayList<>();
        int id = userRepository.get_userID(username);

        String sql = "SELECT content FROM notes WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                notes.add(rs.getString("content"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return notes;

    }

    public boolean deleteNotes(List<String> contents, String username) {

        int id = userRepository.get_userID(username);
        String sql = "DELETE FROM notes WHERE user_id = ? AND content = ?";

        int totalDeleted = 0;


        try (Connection connection = getConnection()) {
            for (String content : contents) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.setString(2, content);
                    totalDeleted += ps.executeUpdate();
                }
            }
            if (totalDeleted > 0) {
                System.out.println("Sikeresen törölve: " + totalDeleted);
                return true;
            } else {
                System.out.println("Nem történt törlés");
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
