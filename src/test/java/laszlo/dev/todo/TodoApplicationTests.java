package laszlo.dev.todo;

import laszlo.dev.todo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TodoApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		boolean result=userRepository.reset_password("laci","kecske");
		assertTrue(result,"A jelszó reset nem sikerült");
	}

}
