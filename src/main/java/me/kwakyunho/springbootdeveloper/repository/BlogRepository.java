package me.kwakyunho.springbootdeveloper.repository;

import me.kwakyunho.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {}