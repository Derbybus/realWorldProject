package com.accede.realWorld.dto;


import com.accede.realWorld.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileDTO {

    private String username;
    private String bio;
    private String image;
    private boolean following;

    public ProfileDTO(Users user, boolean following) {
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.image = user.getImage();
        this.following = following;
    }
}
