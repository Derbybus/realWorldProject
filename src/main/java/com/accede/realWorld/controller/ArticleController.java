package com.accede.realWorld.controller;


import com.accede.realWorld.dto.ArticleDTO;
import com.accede.realWorld.dto.ArticleMapper;
import com.accede.realWorld.dto.TagsResponseDTO;
import com.accede.realWorld.entity.Article;
import com.accede.realWorld.entity.Comments;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.service.ArticleService;
import com.accede.realWorld.service.CommentService;
import com.accede.realWorld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;


    //api to post/add an article
    @PostMapping("/articles")
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article) {
        Users currentUser = userService.getCurrentUser();
        Article createdArticle = articleService.createArticle(currentUser.getUsername(), article);
        return ResponseEntity.ok(new ArticleMapper().toDto(createdArticle, false, 0, false));
    }


    //api to return a single article
    @GetMapping("/articles/{slug}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable("slug") String slug) {
        Users currentUser = userService.getCurrentUser();
        return articleService.getArticle(slug, currentUser.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    //api to update an article
    @PutMapping("/articles/{slug}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable("slug") String slug, @RequestBody Article updatedArticle) {
        Article article = articleService.updateArticle(slug, updatedArticle);
        return ResponseEntity.ok(new ArticleMapper().toDto(article, false, 0, false));
    }


    //api to delete an article
    @DeleteMapping("/articles/{slug}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("slug") String slug) {
        articleService.deleteArticle(slug);
        return ResponseEntity.noContent().build();
    }


    //api to get all articles
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> listArticles() {
        return ResponseEntity.ok(articleService.listArticles());
    }


    //api to return articles created by followed users
    @GetMapping("/articles/feed")
    public ResponseEntity<List<ArticleDTO>> getFeedArticles(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<ArticleDTO> articles = articleService.feedArticles(username, limit, offset);
        return ResponseEntity.ok(articles);
    }



    //api to like an article
    @PostMapping("/{slug}/favorite")
    public ResponseEntity<ArticleDTO> favoriteArticle(@PathVariable String slug) {
        Users currentUser = userService.getCurrentUser();
        articleService.favoriteArticle(currentUser.getUsername(), slug);
        return articleService.getArticle(slug, currentUser.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    //api to unlike an article
    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<ArticleDTO> unLikeArticle(@PathVariable String slug) {
        Users currentUser = userService.getCurrentUser();
        articleService.unfavoriteArticle(currentUser.getUsername(), slug);
        return articleService.getArticle(slug, currentUser.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    //api to add comments to an article
    @PostMapping("/articles/{slug}/comments")
    public ResponseEntity<Comments> addComment(@PathVariable("slug") String slug, @RequestBody String body) {
        Users currentUser = userService.getCurrentUser();
        Comments comment = commentService.addComment(currentUser.getUsername(), slug, body);
        return ResponseEntity.ok(comment);
    }



    //api to get all comments from an article
    @GetMapping("/articles/{slug}/comments")
    public ResponseEntity<List<Comments>> getComments(@PathVariable("slug") String slug) {
        return ResponseEntity.ok(commentService.getComments(slug));
    }



    //api to delete a comment
    @DeleteMapping("/articles/{slug}/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String slug, @PathVariable Long id) {
        commentService.deleteComment(slug, id);
        return ResponseEntity.noContent().build();
    }



    //api to retrieve all tag lists of an article
    @GetMapping("/tags")
    public TagsResponseDTO getAllTags() {
        return articleService.getAllTags();
    }

}
