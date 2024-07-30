package com.accede.realWorld.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ElementCollection
    private Set<String> tagList = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Users author;
    @ManyToMany
    @JoinTable(name = "article_favorites", joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Users> favoritedBy = new HashSet<>();
}
