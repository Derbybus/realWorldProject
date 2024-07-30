package com.accede.realWorld.repository;

import com.accede.realWorld.entity.Article;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {

    Optional<Article> findBySlug(String slug);
    List<Article> findByAuthorUsername(String username);
    List<Article> findByTagList(List<String> tags);
    List<Article> findByAuthorIdIn(List<Long> authorIds);

    /*
    Optional<Article> findBySlug(String slug);
    List<Article> findByAuthorUsername(String username);
    List<Article> findByTagsContaining(String tag);

    @Query("SELECT a FROM Article a JOIN a.favorites f WHERE f.username = :username")
    List<Article> findFavoritedByUser(@Param("username") String username);

    @Query("SELECT a FROM Article a")
    Page<Article> findAll(Pageable pageable);

    Page<Article> findByAuthorIdIn(List<Long> authorIds, Pageable pageable);
     */



//    List<Article> findByTagsContaining(String tag);

//    @Query("SELECT a FROM Article a JOIN a.favorites f WHERE f.username = :username")
//    List<Article> findFavoritedByUser(@Param("username") String username);

//    @Query("SELECT a FROM Article a")
//    Page<Article> findAll(Pageable pageable);

//    @Query("SELECT a FROM Article a WHERE a.author.username = :username")
//    Page<Article> findByAuthorUsername(@Param("username") String username, Pageable pageable);
}
