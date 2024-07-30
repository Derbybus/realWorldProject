package com.accede.realWorld.service;


import com.accede.realWorld.dto.AuthenticationRequest;
import com.accede.realWorld.dto.RegisterDto;
import com.accede.realWorld.dto.UserDTO;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.exceptions.EmailNotFoundException;
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

    //Method to login a user. It checks if the user is already in the system
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



    //It gets details of the current authenticated user logged into the system
//    public Users getCurrentUser() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        // Implement logic to get the currently authenticated user
//        // This is a placeholder and should be replaced with actual authentication logic
////        return userRepository.findByUsername("currentUsername").orElseThrow(() -> new RuntimeException("User not found"));
//    }


    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current Authentication: " + authentication); // Debug log

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            Object principal = authentication.getPrincipal();
            System.out.println("Principal: " + principal); // Debug log

            if (principal instanceof Users) {
                Users user = (Users) principal;
                return new UserDTO(user);
            }
        }
        return null;
    }




    //Method to get the profile of the user




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
        }).orElseThrow();
    }




    /*
      public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId(); // Assuming UserPrincipal has a getId() method
    }

     */



    //Gets profile of the user
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .stream()
                .filter(users -> users.getUsername().equals(username))
                .findAny()
                .orElseThrow();
    }


    /*
     public ResponseEntity<ProfileDTO> getProfile(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(new ProfileDTO(user, followService.isFollowing(currentUser, user))))
                .orElse(ResponseEntity.notFound().build());
    }
     */


}
