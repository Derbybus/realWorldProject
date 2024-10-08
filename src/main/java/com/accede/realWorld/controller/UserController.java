package com.accede.realWorld.controller;


import com.accede.realWorld.config.JwtService;
import com.accede.realWorld.dto.*;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;



    //api request to register a user
    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerUserDto) {
        Users registeredUser = userService.createUser(registerUserDto);
        //generates token for the user
        String token = jwtService.generateToken(registeredUser);
        return ResponseEntity.ok(new AuthenticationResponse(registeredUser, token));
    }


    //api request to login a user
    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequest loginUserDto) {
        Users authenticatedUser = userService.authenticate(loginUserDto);
        //generates token for the user
        String jwtToken = jwtService.generateToken(authenticatedUser);
        return ResponseEntity.ok(new AuthenticationResponse(authenticatedUser,jwtToken));
    }



    //api logic to get the current authenticated user
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser() {
        Users currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(401).build();
        }
    }



    //api to update the current authenticated user
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody Users user, @PathVariable("id") Long id) {
        Users updatedUser = userService.updateUser(user,id);
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }
}
