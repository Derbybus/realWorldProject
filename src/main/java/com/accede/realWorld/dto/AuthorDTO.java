package com.accede.realWorld.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDTO {

    private String username;

    private String bio;

    private String image;

    private boolean following;
}
