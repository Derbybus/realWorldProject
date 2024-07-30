package com.accede.realWorld.service;


import com.accede.realWorld.dto.ArticleDTO;
import com.accede.realWorld.dto.ArticleMapper;
import com.accede.realWorld.entity.Article;
import com.accede.realWorld.entity.Follow;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.repository.ArticleRepository;
import com.accede.realWorld.repository.FollowRepository;
import com.accede.realWorld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Article createArticle(String username, Article article) {
        Users author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        article.setAuthor(author);
        article.setSlug(generateSlug(article.getTitle()));
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

//    public Optional<Article> getArticle(String slug) {
//        return articleRepository.findBySlug(slug);
//    }

    public Optional<ArticleDTO> getArticle(String slug, String currentUsername) {
        Users currentUser = userRepository.findByUsername(currentUsername).orElse(null);
        return articleRepository.findBySlug(slug).map(article -> {
            boolean favorited = article.getFavoritedBy().contains(currentUser);
            int favoritesCount = article.getFavoritedBy().size();
            boolean following = followRepository.findByFollowerAndFollowing(currentUser, article.getAuthor()).isPresent();
            return articleMapper.toDto(article, favorited, favoritesCount, following);
        });
    }


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

    public void deleteArticle(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        articleRepository.delete(article);
    }


//    public List<Article> listArticles() {
//        return articleRepository.findAll();
//    }

    public List<ArticleDTO> listArticles() {
        return articleRepository.findAll().stream()
                .map(article -> articleMapper.toDto(article, false, 0, false))
                .collect(Collectors.toList());
    }

//    public List<Article> feedArticles(String username) {
//        Users user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return articleRepository.findByAuthor(user.getUsername());
//    }

    public List<ArticleDTO> feedArticles(String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> followingIds = followRepository.findByFollowerId(currentUser.getId())
                .stream()
                .map(Follow::getFollowing)
                .map(Users::getId)
                .collect(Collectors.toList());

        return articleRepository.findByAuthorIdIn(followingIds).stream()
                .map(article -> {
                    boolean favorited = article.getFavoritedBy().contains(currentUser);
                    int favoritesCount = article.getFavoritedBy().size();
                    boolean following = followRepository.findByFollowerAndFollowing(currentUser, article.getAuthor()).isPresent();
                    return articleMapper.toDto(article, favorited, favoritesCount, following);
                })
                .collect(Collectors.toList());
    }

    /*
    public List<ArticleDTO> feedArticles(String username, int limit, int offset) {
    Users currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Long> followingIds = followRepository.findByFollowerId(currentUser.getId())
            .stream()
            .map(Follow::getFollowing)
            .map(Users::getId)
            .collect(Collectors.toList());

    Pageable pageable = PageRequest.of(offset, limit);
    Page<Article> articlesPage = articleRepository.findByAuthorIdIn(followingIds, pageable);

    return articlesPage.stream()
            .map(article -> {
                boolean favorited = article.getFavoritedBy().contains(currentUser);
                int favoritesCount = article.getFavoritedBy().size();
                boolean following = followRepository.findByFollowerAndFollowing(currentUser, article.getAuthor()).isPresent();
                return articleMapper.toDto(article, favorited, favoritesCount, following);
            })
            .collect(Collectors.toList());
}

     */

    public void favoriteArticle(String username, String slug) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        article.getFavoritedBy().add(user);
        articleRepository.save(article);
    }


    public void unfavoriteArticle(String username, String slug) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        article.getFavoritedBy().remove(user);
        articleRepository.save(article);
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")  // Remove all non-alphanumeric characters except spaces and hyphens
                .replaceAll("\\s+", "-")         // Replace spaces with hyphens
                .replaceAll("-+", "-")           // Replace multiple hyphens with a single hyphen
                .replaceAll("^-|-$", "");        // Remove leading and trailing hyphens
    }

}
