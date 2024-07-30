//package com.accede.realWorld.controller;
//
//
//import com.accede.realWorld.dto.ProfileDTO;
//import com.accede.realWorld.entity.Users;
//import com.accede.realWorld.service.FollowService;
//import com.accede.realWorld.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class ProfileController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private FollowService followService;
//
//
////    @GetMapping("/{username}")
////    public ResponseEntity<ProfileDTO> getProfile(@PathVariable String username) {
////        Users currentUser = userService.getCurrentUser();
////        return userService.getUserByUsername(username)
////                .map(user -> ResponseEntity.ok(new ProfileDTO(user, followService.isFollowing(currentUser.getUsername(), username))))
////                .orElse(ResponseEntity.notFound().build());
////    }
//
//
//
//
//    //api to follow a user
//    @PostMapping("/profiles/{username}/follow")
//    public ResponseEntity<ProfileDTO> followUser(@PathVariable("username") String username) {
//        Users currentUser = userService.getCurrentUser();
//        followService.followUser(currentUser.getUsername(), username);
//        Users followedUser = userService.getUserByUsername(username);
//        return ResponseEntity.ok(new ProfileDTO(followedUser, true));
//    }
//
//
//
//    //api to unfollow a user
//    @DeleteMapping("/profiles/{username}/follow")
//    public ResponseEntity<ProfileDTO> unfollowUser(@PathVariable("username") String username) {
//        Users currentUser = userService.getCurrentUser();
//        followService.unfollowUser(currentUser.getUsername(), username);
//        Users unfollowedUser = userService.getUserByUsername(username);
//        return ResponseEntity.ok(new ProfileDTO(unfollowedUser, false));
//    }
//}
