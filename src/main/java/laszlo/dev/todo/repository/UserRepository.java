package laszlo.dev.todo.repository;

import jakarta.servlet.http.HttpSession;
import laszlo.dev.todo.entities.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserRepository {


    private String sql = "";

    private Connection getConnection() throws SQLException {

        final String url = "jdbc:mysql://localhost:3306/user_datas";
        String username = "laci";
        String password = "laci";
        return DriverManager.getConnection(url, username, password);
    }

    public boolean testconnection(){

        try{
           Connection connection= getConnection();
           return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isbanned(String username) {


        String sql = "select isbanned from users where username=?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getBoolean("isbanned");
            }
            else {
                return false;
            }




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean bannusers(String username) {

        String sql = "update  users set isbanned =true where username =?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean unbanusers(String username) {
        String sql = "update  users set isbanned =false where username =?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int get_userID(String username) {

        String sql = "select id from users where username=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {

                return resultSet.getInt("id");
            } else {

                return -1;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public List<Users> findAllUsers() {
        String sql = "SELECT username, email, role, registered_at, last_login,isbanned FROM users";
        List<Users> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Users user = new Users();
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setRegisteredAt(rs.getString("registered_at"));
                user.setLastLogin(rs.getString("last_login"));
                user.setIsbanned(rs.getBoolean("isbanned"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public boolean check_username(String username) {
        String sql = "SELECT username FROM users WHERE username=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public void register_user(Users user) {
        String insertsql = "INSERT INTO users(username,email,password,registered_at) VALUES(?,?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(insertsql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(4, now.format(formatter));

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public boolean reset_password(String username, String password) {
        String sql = "update users set password=? where username =?";
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ) {
            String hashed_password = password_hash(password);

            preparedStatement.setString(1, hashed_password);
            preparedStatement.setString(2, username);

            int modositott_sorok = preparedStatement.executeUpdate();
            ;

            return modositott_sorok > 0;
        } catch (SQLException exception) {
            System.out.println(exception);

            throw new RuntimeException();
        }
    }

    public String password_hash(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public boolean check_password(String rawPassword, String encoded_password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, encoded_password);
    }

    public boolean delete_users(String username) {
        String sql = "delete from users where username =?";
        try (

                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ) {

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }

    }

    public Users findByUsername(String username) {
        String sql = "SELECT username, email, password FROM users WHERE username=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Users user = new Users();
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            }

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        return null;
    }


    public boolean is_admin(HttpSession session) {
        String username = (String) session.getAttribute("user");
        try (
                Connection connection = getConnection();

                PreparedStatement preparedStatement = connection.prepareStatement("SELECT role from users WHERE username=?")
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("admin")) {
                    return true;
                } else {
                    return false;
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void updateLastLogin(String username) {
        String sql = "UPDATE users SET last_login=? WHERE username=?";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, now.format(formatter));
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }


}

