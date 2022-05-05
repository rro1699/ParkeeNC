package com.example.ncproject.Controllers;

import com.example.ncproject.Models.User;
import com.example.ncproject.Repository.UserRepository;
import com.example.ncproject.add.TokenVerifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository=userRepository;
        TokenVerifier.Init();
    }

    @PostMapping("/users/addUser")
    public ResponseEntity addUser(@RequestBody String idToken){
        User user = TokenVerifier.getNewUser(idToken);
        if(user!=null){
            userRepository.save(user);
            ResponseCookie cookie = ResponseCookie.from("user-id", user.getId()).path("/").build();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.SET_COOKIE,cookie.toString())
                    .build();
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
