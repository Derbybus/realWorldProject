package com.accede.realWorld.service;


import com.accede.realWorld.entity.Follow;
import com.accede.realWorld.entity.Users;
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

    @Transactional
    public void followUser(String currentUsername, String usernameToFollow) {
        Users currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        Users userToFollow = userRepository.findByUsername(usernameToFollow)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        if (followRepository.findByFollowerAndFollowing(currentUser, userToFollow).isPresent()) {
            throw new RuntimeException("Already following this user");
        }

        Follow follow = new Follow();
        follow.setFollower(currentUser);
        follow.setFollowing(userToFollow);
        followRepository.save(follow);
    }


    @Transactional
    public void unfollowUser(String currentUsername, String usernameToUnfollow) {
        Users currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        Users userToUnfollow = userRepository.findByUsername(usernameToUnfollow)
                .orElseThrow(() -> new RuntimeException("User to unfollow not found"));

        Follow follow = followRepository.findByFollowerAndFollowing(currentUser, userToUnfollow)
                .orElseThrow(() -> new RuntimeException("Follow relationship not found"));
        followRepository.delete(follow);
    }

    public boolean isFollowing(String currentUsername, String usernameToCheck) {
        Users currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        Users userToCheck = userRepository.findByUsername(usernameToCheck)
                .orElseThrow(() -> new RuntimeException("User to check not found"));

        return followRepository.findByFollowerAndFollowing(currentUser, userToCheck).isPresent();
    }

}
