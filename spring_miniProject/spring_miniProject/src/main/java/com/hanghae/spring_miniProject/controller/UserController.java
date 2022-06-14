package com.hanghae.spring_miniProject.controller;

import com.hanghae.spring_miniProject.dto.SignupRequestDto;
import com.hanghae.spring_miniProject.model.User;
import com.hanghae.spring_miniProject.repository.UserRepository;
import com.hanghae.spring_miniProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    //회원가입
    @PostMapping("/user/signup")
    public String registerUser(@RequestBody SignupRequestDto requestDto) {
        try{
            userService.registerUser(requestDto);
            return "Success";
        }
        catch(IllegalArgumentException e){
            return e.getMessage();

        }
    }

    //아이디 중복 체크
    @GetMapping("/user/idCheck/{username}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username){
        Optional<User> found = userRepository.findByUsername(username);
        if(found.isPresent()){
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
