package com.accede.realWorld.repository;

import com.accede.realWorld.entity.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;


@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {

    Optional<Article> findBySlug(String slug);
    Page<Article> findByAuthorIdIn(List<Long> authorIds, Pageable pageable);


}
