package com.accede.realWorld.service;


import com.accede.realWorld.dto.AuthenticationRequest;
import com.accede.realWorld.dto.AuthenticationResponse;
import com.accede.realWorld.dto.RegisterDto;
import com.accede.realWorld.dto.UserDTO;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.exceptions.EmailNotFoundException;
import com.accede.realWorld.exceptions.IdNotFoundException;
import com.accede.realWorld.exceptions.UserNotFoundException;
import com.accede.realWorld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    //Method to create or register a new user into the system
    public Users createUser(RegisterDto user){
        Users users = Users.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        return userRepository.save(users);

    }

    //Method to log in a user. It checks if the user is already in the system before it logs the user in
    public Users authenticate(AuthenticationRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("User email not found"));
    }



    //Method to get details of the current authenticated user logged into the system
    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        }
        return null;
    }


    //Updates details of an existing user
    public Users updateUser(Users users, Long id) {
        return userRepository.findById(id).map(user -> {
            if (users.getUsername() != null){
                user.setUsername(users.getUsername());
            }
            if (users.getEmail() != null){
                user.setEmail(users.getEmail());
            }
            if (users.getPassword() != null){
                user.setPassword(users.getPassword());
            }
            if (users.getBio() != null){
                user.setBio(users.getBio());
            }
            if (users.getImage() != null){
                user.setImage(users.getImage());
            }
            return userRepository.save(user);
        }).orElseThrow(()-> new IdNotFoundException("User id not found"));
    }



    //Finds the username to get the profile of the user
    public Optional<Users> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .stream()
                .filter(users -> users.getUsername().equals(username))
                .findAny();
    }


}
