package me.kwakyunho.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.kwakyunho.springbootdeveloper.domain.Article;

@NoArgsConstructor
@AllArgsConstructor
public class AddArticleRequest {

    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}