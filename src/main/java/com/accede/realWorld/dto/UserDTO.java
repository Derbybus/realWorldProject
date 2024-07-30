package com.accede.realWorld.dto;


import com.accede.realWorld.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String username;
    private String email;
    private String bio;
    private String image;
    private String token;


    public UserDTO(Users user){
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.image = user.getImage();


    }


}
