package laszlo.dev.todo.entities;


import java.util.List;

public class Users {

    private String username;
    private String password;
    private String email;
    private String role;
    private String registeredAt;
    private String lastLogin;
    private boolean isbanned;
    private List<String> notes;

    public Users() {

    }

    public Users(String username, String password, String email, String role, String registeredAt, String lastLogin, List<String> notes,Boolean isbanned) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.registeredAt = registeredAt;
        this.lastLogin = lastLogin;
        this.notes = notes;
        this.isbanned=isbanned;
    }

    public boolean isIsbanned() {
        return isbanned;
    }

    public void setIsbanned(boolean isbanned) {
        this.isbanned = isbanned;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }


    public List<String> getNotes() {
        return notes;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public String getLastLogin() {
        return lastLogin;
    }


}
