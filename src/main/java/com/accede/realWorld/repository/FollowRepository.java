package com.accede.realWorld.repository;

import com.accede.realWorld.entity.Follow;
import com.accede.realWorld.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findByFollowerAndFollowing(Users follower, Users following);

    List<Follow> findByFollowerId(Long followerId);
}
