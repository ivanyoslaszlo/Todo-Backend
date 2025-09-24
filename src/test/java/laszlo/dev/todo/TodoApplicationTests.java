package laszlo.dev.todo;

import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void password_Reset() {

        boolean result = userRepository.reset_password("laci", "laci");
        assertTrue(result, "A jelszó reset nem sikerült");
    }

}



