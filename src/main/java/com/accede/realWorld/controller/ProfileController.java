package com.accede.realWorld.controller;


import com.accede.realWorld.dto.ProfileDTO;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.service.FollowService;
import com.accede.realWorld.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);



    //Gets profile of the logged in user
    @GetMapping("/profiles/{username}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable("username") String username) {
        // Get the current authenticated user
        String currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        logger.debug("Current authenticated username: {}", currentUsername);
        //checks if current user is following the person
        return userService.getUserByUsername(username)
                .map(user -> {
                    boolean isFollowing = followService.isFollowing(currentUsername, username);
                    return ResponseEntity.ok(new ProfileDTO(user, isFollowing));
                })
                .orElse(ResponseEntity.notFound().build());
    }



    //api to follow a user
    //uses spring security's authentication principal to get the current logged-in user
    @PostMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileDTO> followUser(@PathVariable("username") String username, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String currentUsername = userDetails.getUsername();
        followService.followUser(currentUsername, username);
        Users followedUser = userService.getUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new ProfileDTO(followedUser, true));
    }


    //api to unfollow a user
    //uses spring security's authentication principal to get the current logged-in user
    @DeleteMapping("/profiles/{username}/unfollow")
    public ResponseEntity<ProfileDTO> unfollowUser(@PathVariable("username") String username, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String currentUsername = userDetails.getUsername();
        followService.unfollowUser(currentUsername, username);
        Users unfollowedUser = userService.getUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new ProfileDTO(unfollowedUser, false));
    }



}
