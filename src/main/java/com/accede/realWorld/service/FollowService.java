package com.accede.realWorld.service;


import com.accede.realWorld.entity.Follow;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.exceptions.UserNotFoundException;
import com.accede.realWorld.repository.FollowRepository;
import com.accede.realWorld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;


    //Method to follow a user. First checks if the user is in the system and later checks
    //if the followed user is in the system before it proceeds.

    public void followUser(String username, String userToFollow) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));
        Users followUser = userRepository.findByUsername(userToFollow)
                .orElseThrow(() -> new UserNotFoundException("User to follow not found"));
        //checks if both users are following each other
        if (followRepository.findByFollowerAndFollowing(currentUser, followUser).isPresent()) {
            throw new RuntimeException("Already following this user");
        }

        //Enables following if users aren't following each other
        Follow follow = new Follow();
        follow.setFollower(currentUser);
        follow.setFollowing(followUser);
        followRepository.save(follow);
    }



    //Method to unfollow a user
    @Transactional
    public void unfollowUser(String username, String unfollowUser) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));
        Users userToUnfollow = userRepository.findByUsername(unfollowUser)
                .orElseThrow(() -> new UserNotFoundException("User to unfollow not found"));

        Follow follow = followRepository.findByFollowerAndFollowing(currentUser, userToUnfollow)
                .orElseThrow(() -> new RuntimeException("Follow relationship not found"));
        //removes/deletes/unfollows the person
        followRepository.delete(follow);
    }


    //a boolean method which checks if users are following each other when a profile is retrieved.
    //it gives either a true or false answer
    public boolean isFollowing(String username, String followUsername) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));
        Users userToCheck = userRepository.findByUsername(followUsername)
                .orElseThrow(() -> new UserNotFoundException("User to check not found"));

        return followRepository.findByFollowerAndFollowing(currentUser, userToCheck).isPresent();
    }

}
