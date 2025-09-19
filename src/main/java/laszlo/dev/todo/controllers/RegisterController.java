package laszlo.dev.todo.controllers;

import laszlo.dev.todo.entities.Users;
import laszlo.dev.todo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class RegisterController {

    UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/register")
    public String register_user(@RequestBody Users users) {

      if (userService.registerUser(users))
      {
          return "siker";
      }else
      {
       return "foglalt";
      }




    }


}
