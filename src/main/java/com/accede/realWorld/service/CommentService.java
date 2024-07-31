package com.accede.realWorld.service;


import com.accede.realWorld.entity.Article;
import com.accede.realWorld.entity.Comments;
import com.accede.realWorld.entity.Users;
import com.accede.realWorld.repository.ArticleRepository;
import com.accede.realWorld.repository.CommentRepository;
import com.accede.realWorld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ArticleRepository articleRepository;



    //Add a comment to an article
    public Comments addComment(String username, String slug, String body) {
        Users author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Comments comment = new Comments();
        comment.setAuthor(author);
        comment.setArticle(article);
        comment.setBody(body);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }


    //Gets the list of comments under an article
    public List<Comments> getComments(String slug) {
        return commentRepository.findByArticleSlug(slug);
    }



    //Deletes a comment
    public void deleteComment(String slug, Long id) {
        // Ensure the article exists and the comment belongs to it
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Comments comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getArticle().equals(article)) {
            throw new IllegalArgumentException("Comment does not belong to the specified article");
        }

        commentRepository.deleteById(id);
    }
}
