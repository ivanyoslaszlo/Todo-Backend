package laszlo.dev.todo.repository;

import laszlo.dev.todo.entities.Users;
import org.apache.catalina.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotesRepository {

    private final String url = "jdbc:sqlite:user.datas.db";

    public boolean createNote(String username, String content) {
        String sql = "INSERT INTO notes (username, content) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, content);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Hiba a jegyzet mentése közben: " + e.getMessage());

            return false;

        }

    }


    public List<String> getNotes(String username) {

        List<String> notes = new ArrayList<>();

        String url = "jdbc:sqlite:user.datas.db";
        String sql = "SELECT content FROM notes WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
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
        String url = "jdbc:sqlite:user.datas.db";
        String sql = "DELETE FROM notes WHERE username = ? AND content = ?";

        int totalDeleted = 0;


        try (Connection connection = DriverManager.getConnection(url)) {
            for (String content : contents) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, username);
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

    public List<Users> findAll_user() {

        String sql = "SELECT * from users;";
        List<Users> users_list = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet resultSet = ps.executeQuery()) {


            while (resultSet.next()) {

                Users users = new Users();
                users.setUsername(resultSet.getString("username"));
                users.setEmail(resultSet.getString("email"));
                users.setRole(resultSet.getString("role"));
                users.setLastLogin(resultSet.getString("last_login"));
                users.setRegisteredAt(resultSet.getString("registered_at"));
                users_list.add(users);
            }


        } catch (SQLException e) {

            throw new RuntimeException("Hiba a felhasználók lekérdezése közben!");
        }

        return users_list;
    }

}
