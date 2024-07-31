package com.accede.realWorld.service;


import com.accede.realWorld.dto.ArticleDTO;
import com.accede.realWorld.dto.ArticleMapper;
import com.accede.realWorld.dto.TagsResponseDTO;
import com.accede.realWorld.entity.Article;
import com.accede.realWorld.entity.Follow;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.repository.ArticleRepository;
import com.accede.realWorld.repository.FollowRepository;
import com.accede.realWorld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ArticleMapper articleMapper;


    //Creates an article
    public Article createArticle(String username, Article article) {
        Users author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        article.setAuthor(author);
        article.setSlug(generateSlug(article.getTitle()));
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }



    //Gets article and shows if a user has liked it, the number of times the article has been liked
    // and if user is following the author
    public Optional<ArticleDTO> getArticle(String slug, String currentUsername) {
        Users currentUser = userRepository.findByUsername(currentUsername).orElse(null);
        return articleRepository.findBySlug(slug).map(article -> {
            boolean favorited = article.getFavoritedBy().contains(currentUser);
            int favoritesCount = article.getFavoritedBy().size();
            boolean following = followRepository.findByFollowerAndFollowing(currentUser, article.getAuthor()).isPresent();
            return articleMapper.toDto(article, favorited, favoritesCount, following);
        });
    }



    //Updates article of the author
    public Article updateArticle(String slug, Article updatedArticle) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setTitle(updatedArticle.getTitle());
        article.setDescription(updatedArticle.getDescription());
        article.setBody(updatedArticle.getBody());
        article.setTagList(updatedArticle.getTagList());
        article.setSlug(generateSlug(updatedArticle.getTitle()));
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }



    //deletes an article
    public void deleteArticle(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        articleRepository.delete(article);
    }




    //Gets a multiple lists of articles, the number of times it's been liked, following count and likes on the article
    public List<ArticleDTO> listArticles() {
        return articleRepository.findAll().stream()
                .map(article -> articleMapper.toDto(article, false, 0, false))
                .collect(Collectors.toList());
    }



    //Returns multiple articles created by followed users, ordered by most recent first.
    public List<ArticleDTO> feedArticles(String username, int limit, int offset) {
    Users currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    //Get list of user IDs the current user is following
    List<Long> followingIds = followRepository.findByFollowerId(currentUser.getId())
            .stream()
            .map(Follow::getFollowing)
            .map(Users::getId)
            .collect(Collectors.toList());

    //Gets the limit and offset of articles to appear when called
    Pageable pageable = PageRequest.of(offset, limit);
    Page<Article> articlesPage = articleRepository.findByAuthorIdIn(followingIds, pageable);

    //converts the article to the articleMapper dto
    return articlesPage.stream()
            .map(article -> {
                boolean favorited = article.getFavoritedBy().contains(currentUser);
                int favoritesCount = article.getFavoritedBy().size();
                boolean following = followRepository.findByFollowerAndFollowing(currentUser, article.getAuthor()).isPresent();
                return articleMapper.toDto(article, favorited, favoritesCount, following);
            })
            .collect(Collectors.toList());
}



    //Method to like an article
    public void favoriteArticle(String username, String slug) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        article.getFavoritedBy().add(user);
        articleRepository.save(article);
    }


    //Method to unlike an article
    public void unfavoriteArticle(String username, String slug) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        article.getFavoritedBy().remove(user);
        articleRepository.save(article);
    }


    //A private method to generate a slug from the title
    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")  // Remove all non-alphanumeric characters except spaces and hyphens
                .replaceAll("\\s+", "-")         // Replace spaces with hyphens
                .replaceAll("-+", "-")           // Replace multiple hyphens with a single hyphen
                .replaceAll("^-|-$", "");        // Remove leading and trailing hyphens
    }


    //Returns a list of tags
    public TagsResponseDTO getAllTags() {
        List<Article> articles = articleRepository.findAll();
        List<String> allTags = articles.stream()
                .flatMap(article -> article.getTagList().stream())
                .distinct()
                .collect(Collectors.toList());
        return new TagsResponseDTO(allTags);
    }
}
