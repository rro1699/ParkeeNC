package com.example.ncproject.Services;

import com.example.ncproject.DAO.Models.User;
import com.example.ncproject.DAO.Repository.UserRepository;
import com.example.ncproject.Services.ServiceUtils.TokenVerifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        TokenVerifier.Init();
    }

    public ResponseEntity addNewUser(String idToken){
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
