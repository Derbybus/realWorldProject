package com.accede.realWorld.dto;

import com.accede.realWorld.entity.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationResponse {

    private String username;
    private String email;
    private String bio;
    private String image;
    private String token;

    public AuthenticationResponse(Users user, String token) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.image = user.getImage();
        this.token = token;

    }
}
