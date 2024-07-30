package com.accede.realWorld.dto;


import com.accede.realWorld.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleDTO toDto(Article article, boolean favorited, int favoritesCount, boolean following) {
        ArticleDTO dto = new ArticleDTO();
        dto.setSlug(article.getSlug());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setBody(article.getBody());
        dto.setTagList(article.getTagList());
        dto.setCreatedAt(String.valueOf(article.getCreatedAt()));
        dto.setUpdatedAt(String.valueOf(article.getUpdatedAt()));
        dto.setFavorited(favorited);
        dto.setFavoritesCount(favoritesCount);

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setUsername(article.getAuthor().getUsername());
        authorDTO.setBio(article.getAuthor().getBio());
        authorDTO.setImage(article.getAuthor().getImage());
        authorDTO.setFollowing(following);

        dto.setAuthor(authorDTO);
        return dto;
    }
}
