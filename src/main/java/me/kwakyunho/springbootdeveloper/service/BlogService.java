package me.kwakyunho.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.kwakyunho.springbootdeveloper.domain.Article;
import me.kwakyunho.springbootdeveloper.dto.AddArticleRequest;
import me.kwakyunho.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
}
