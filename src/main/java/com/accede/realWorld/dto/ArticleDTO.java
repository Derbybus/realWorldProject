package com.accede.realWorld.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleDTO {

    private String slug;

    private String title;

    private String description;

    private String body;

    private Set<String> tagList;

    private String createdAt;

    private String updatedAt;

    private boolean favorited;

    private int favoritesCount;

    private AuthorDTO author;
}
