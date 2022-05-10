package com.example.ncproject.Controllers;

import com.example.ncproject.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/addUser")
    public ResponseEntity addUser(@RequestBody String idToken){
        return userService.addNewUser(idToken);
    }


}
