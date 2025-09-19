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

        private final String url = "jdbc:sqlite:user.datas.db";
        private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        public List<Users> findAllUsers() {
            String sql = "SELECT username, email, role, registered_at, last_login FROM users";
            List<Users> users = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Users user = new Users();
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setRegisteredAt(rs.getString("registered_at"));
                    user.setLastLogin(rs.getString("last_login"));
                    users.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return users;
        }

        public boolean check_username(String username) {
            String sql = "SELECT username FROM users WHERE username=?";
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        }
        public void save(Users user) {
            String insertsql = "INSERT INTO users(username,email,password,registered_at) VALUES(?,?,?,?)";

            try (Connection connection = DriverManager.getConnection(url);
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


        public String password_hash(String password){

            return encoder.encode(password);
        }
        public boolean check_password(String rawPassword, String encoded_password) {
            return encoder.matches(rawPassword, encoded_password);
        }


        public Users findByUsername(String username) {
            String sql = "SELECT username, email, password FROM users WHERE username=?";
            try (Connection connection = DriverManager.getConnection(url);
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




        public boolean is_admin(HttpSession session){
            String username=(String)session.getAttribute("user");
            try(
                    Connection connection=DriverManager.getConnection("jdbc:sqlite:user.datas.db");

                    PreparedStatement preparedStatement=connection.prepareStatement("SELECT role from users WHERE username=?")
            ){
                preparedStatement.setString(1,username);
                ResultSet resultSet=preparedStatement.executeQuery();

                if (resultSet.next()){
                    String role = resultSet.getString("role");
                    if (role.equals("admin")){
                        return true;
                    }else{
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

            try (Connection connection = DriverManager.getConnection(url);
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

